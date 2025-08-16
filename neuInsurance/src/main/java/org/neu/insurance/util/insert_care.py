import pandas as pd
import pymysql
import re

def price_to_float(x):
    # 如果是数字，正常转float，否则返回0.0
    try:
        # 只保留数字和小数点
        num = re.findall(r"[0-9.]+", str(x))
        return float(num[0]) if num else 0.0
    except Exception:
        return 0.0

def import_care_excel_to_mysql(file_path, db_config):
    df = pd.read_excel(file_path, dtype=str)
    df = df.fillna('')

    # 字段映射
    col_map = {
        '财务分类': 'category',
        '项目编码': 'service_code',
        '国家编码': 'national_code',
        '项目名称': 'service_name',
        '项目内涵': 'service_content',
        '除外内容': 'exclude_content',
        '计价单位': 'unit_type',
        '价格': 'price',
        '说明': 'remark'
    }
    df = df[[col for col in col_map if col in df.columns]].rename(columns=col_map)

    # 价格清洗
    df['remark'] = df.apply(
        lambda row: f"{row['remark']}（价格字段原值：{row['price']}）" if not str(row['price']).replace('.', '', 1).isdigit() else row['remark'],
        axis=1
    )
    df['price'] = df['price'].apply(price_to_float)

    # 自动补全缺失字段
    df['status'] = 1
    df['create_by'] = 'admin'
    df['update_by'] = 'admin'

    # 连接MySQL
    conn = pymysql.connect(**db_config)
    cursor = conn.cursor()

    sql = """
INSERT INTO medical_service (
    service_code, national_code, service_name, service_content, exclude_content, unit_type, price, category, status, create_by, update_by, remark
) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
ON DUPLICATE KEY UPDATE
    national_code=VALUES(national_code),
    service_name=VALUES(service_name),
    service_content=VALUES(service_content),
    exclude_content=VALUES(exclude_content),
    unit_type=VALUES(unit_type),
    price=VALUES(price),
    category=VALUES(category),
    status=VALUES(status),
    create_by=VALUES(create_by),
    update_by=VALUES(update_by),
    remark=VALUES(remark)
"""

    for _, row in df.iterrows():
        cursor.execute(sql, (
            row['service_code'],
            row['national_code'],
            row['service_name'],
            row['service_content'],
            row['exclude_content'],
            row['unit_type'],
            row['price'],
            row['category'],
            row['status'],
            row['create_by'],
            row['update_by'],
            row['remark']
        ))
    conn.commit()
    cursor.close()
    conn.close()
    print(f"导入成功，共导入{len(df)}条数据。")

if __name__ == "__main__":
    db_config = {
        'host': 'localhost',
        'user': 'root',
        'password': 'root',
        'database': 'neuinsurance',
        'charset': 'utf8mb4'
    }
    import_care_excel_to_mysql('诊疗项目.xlsx', db_config)