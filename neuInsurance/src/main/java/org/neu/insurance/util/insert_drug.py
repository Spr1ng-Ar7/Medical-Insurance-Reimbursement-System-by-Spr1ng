import pandas as pd
import pymysql

def import_drug_excel_to_mysql(file_path, db_config):
    df = pd.read_excel(file_path, dtype=str)
    df = df.fillna('')

    # 字段映射
    col_map = {
        '分类': 'drug_type',
        '医保中文名称': 'drug_name',
        '商品名': 'trade_name',
        '规格': 'specification',
        '单位': 'unit',
        '生产企业': 'manufacturer',
        '支付标准': 'price',
        '备注': 'remark'
    }
    # 只保留需要的列并重命名
    df = df[[col for col in col_map if col in df.columns]].rename(columns=col_map)

    # 自动补全缺失字段
    df['drug_code'] = ['DRUG%05d' % (i+1) for i in range(len(df))]  # 可自定义生成规则
    df['self_pay_ratio'] = 0
    df['status'] = 1
    df['create_by'] = 'admin'
    df['update_by'] = 'admin'

    # 类型转换
    df['price'] = df['price'].replace('', '0').astype(float)
    df['self_pay_ratio'] = df['self_pay_ratio'].astype(float)
    df['status'] = df['status'].astype(int)

    # 连接MySQL
    conn = pymysql.connect(**db_config)
    cursor = conn.cursor()

    sql = """
    INSERT INTO drug (
        drug_code, drug_name, trade_name, drug_type, self_pay_ratio, specification, unit, price, manufacturer, status, create_by, update_by, remark
    ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
    """

    for _, row in df.iterrows():
        cursor.execute(sql, (
            row['drug_code'],
            row['drug_name'],
            row['trade_name'],
            row['drug_type'],
            row['self_pay_ratio'],
            row['specification'],
            row['unit'],
            row['price'],
            row['manufacturer'],
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
    import_drug_excel_to_mysql('医保药品.xls', db_config)