import pandas as pd
import pymysql

def import_icd10_excel_to_mysql(file_path, db_config):
    df = pd.read_excel(file_path, dtype=str)
    df = df.fillna('')

    # 字段映射
    col_map = {
        '国际ICD编码': 'icd_code',
        '疾病编码': 'disease_code',
        '疾病名称': 'disease_name',
        '疾病分类': 'disease_category',
        '描述': 'description'  # 如果有“描述”列
    }
    # 只保留需要的列并重命名
    df = df[[col for col in col_map if col in df.columns]].rename(columns=col_map)

    # 自动补全缺失字段
    df['status'] = 1
    df['create_by'] = 'admin'
    df['update_by'] = 'admin'
    df['remark'] = ''

    # 连接MySQL
    conn = pymysql.connect(**db_config)
    cursor = conn.cursor()

    sql = """
    INSERT INTO disease (
        icd_code, disease_code, disease_name, disease_category, description, status, create_by, update_by, remark
    ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
    ON DUPLICATE KEY UPDATE
        disease_name=VALUES(disease_name),
        disease_category=VALUES(disease_category),
        description=VALUES(description),
        status=VALUES(status),
        update_by=VALUES(update_by),
        remark=VALUES(remark)
    """

    for _, row in df.iterrows():
        cursor.execute(sql, (
            row['icd_code'],
            row['disease_code'],
            row['disease_name'],
            row['disease_category'],
            row['description'] if 'description' in row else '',
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
    import_icd10_excel_to_mysql('国际疾病分类标准编码(ICD-10).xlsx', db_config)