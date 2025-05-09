import time
import psycopg2 
from psycopg2 import sql

file_path = "Loader/resources/Data.sql"
def normal_load(cnt):
    start_time = time.time()
    try:
        conn = psycopg2.connect(
            host="localhost", 
            port=5432, 
            user="proj", 
            password="123456",  
            database="proj1"
        )
        conn.autocommit = True  
        
        with conn.cursor() as cur:
            with open(file_path, 'r', encoding='utf-8') as f:
                sql_commands = f.read()
            
            
            commands = [cmd.strip() for cmd in sql_commands.split(";") if cmd.strip()]
            
            for command in commands:
                try:
                    cur.execute(command)
                    cnt+=1
                except Exception as e:
                    print(f"wrongful execution") 
            print("SQL instruction finished")
            
    except Exception as e:
        print(f"database error: {e}")
    finally:
        if 'conn' in locals():
            conn.close()
    
    end_time = time.time()
    execution_time=end_time-start_time
    records_per_second=cnt/execution_time
    print("total sql:",cnt)
    print(f"execution time: {execution_time:.2f}s")
    print(f"records per second: {records_per_second:.2f}records/s")
    return cnt

def read_insert(cnt):
    start_time = time.time()
    conn = psycopg2.connect(
            host="localhost", 
            port=5432, 
            user="proj", 
            password="123456",  
            database="proj1"
        )
    read_file=open(file_path).read()
    cur=conn.cursor()
    cur.execute(read_file)
    conn.commit()
    cur.close()
    conn.close()
    end_time = time.time()
    print("read and insert finish")
    print("execution time: ",end_time - start_time)
    print("records per second: ",cnt/(end_time-start_time))

if __name__ == '__main__':
    cnt=0
    cnt=normal_load(cnt)
    read_insert(cnt)