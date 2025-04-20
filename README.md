# CS307 Database Project1
**成员：沈泓立（12311016），郑袭明（12311011）**

### 成员分工及贡献百分比

**沈泓立**：

- E-R图绘制
- Java数据筛选与导入
- Python和C++数据导入框架与编写
- Python和C++部分多线程优化以及disable trigger和entrigger的对比测试
- 多平台导入数据测试（Windows、macOS）
- 项目报告写作

**郑袭明**：

- 数据库建表设计
- Java数据筛选与导入
- 基于Java的多种导入优化，如Batch、多线程等，并进行比较
- 项目相关的accuracy checking SQL语句编写
- 不同的Data Volume测试优化
- 进行非Postgres导入测试（OpenGauss，MySQL）
- 项目报告写作

贡献百分比相同，均为**50%**

### Task 1: E-R Diagram

本小组使用 [drawio](https://www.diagrams.net/) 绘图工具，绘制本项目的 E-R 图，截图如下：

![](https://github.com/renmiamu/CS307_Principles_of_Database_System_Project1/er_diagram.drawio.png)

### Task 2: Relational Database Design

本项目使用 [`DDL.sql`](https://github.com/renmiamu/CS307_Principles_of_Database_System_Project1/blob/main/DDL.sql) 文件创建数据表，使用 `PostgreSQL`的 `DDL` 语法编写。（后续在MySQL导入中，使用`MySQL`的语法改写进行导入。）

#### 数据库设计

使用 [`DataGrip`](https://www.jetbrains.com/datagrip/) 创建数据表并全选后通过右键 `Diagram > Show Diagram` 显示如下数据表设计及关系。

![](https://github.com/renmiamu/CS307_Principles_of_Database_System_Project1/blob/main/TABELS.png)

#### 设计思路及说明

##### 数据表及其各列含义说明

在整个项目中共创建了 8 个数据表，数据表和其中各列、外键的含义如下：

1. **Supply_center** 表存储供应中心的信息。包括供应中心名称 `supply_center`（主键），所属区域的管理员 `director`。
2. **Region** 表存储地区信息。包括城市编号 `region_id`（主键），国家 `country`，城市名 `city`。
3. **Enterprise** 表存储公司的信息。包括公司编号 `company_id`（主键），客户公司名 `client_enterprise`，所属行业 `industry`，关联的供应中心 `supply_center`（外键），所在地区编号 `region_id`（外键）。
4. **Contract** 表存储合同信息。包括合同编号 `contract_number`（主键），合同签订日期 `contract_date`，签约企业编号 `enterprise`（外键）。
5. **Products** 表存储产品信息。包括产品编码 `product_code`（主键），产品名称 `product_name`。
6. **Product_models** 表存储产品型号的相关信息。包括产品型号编号 `model_id`（主键），型号名称 `product_model_name`，所属产品的编码 `product_code`（外键），该型号的单价 `unit_price`。
7. **Salesman** 表存储销售人员的信息。包括销售员编号 `salesman_id`（主键），工号 `salesman_number`，姓名 `salesman_name`，性别 `gender`，年龄 `age`，手机号 `mobile_number`，所属供应中心 `supply_center`（外键）。
8. **Contract_details** 表存储合同的详细内容。包括合同编号 `contract_id`、产品型号编号 `model_id`（复合主键）、销售员编号 `salesman_id`（外键），销售数量 `quantity`，预计交货日期 `estimated_deliver_date`，付款到账日期 `lodgement_date`。

##### 数据库构建的合理性

- **满足三大范式**
  - 通过示意图可以看到，每个数据表的每一列都是不可分割的，仅有一个值。
  - 每个数据表都有主关键字，且主关键字都是 `UNIQUE` 的，其它数据元素能和主关键字一一对应。
  - 通过设计外键连接，我们将同一数据表中具有“传递”关系的数据列设计成不同的表格进行设计，不存在非关键字段对任一候选关键字段的传递函数依赖。
  - 可见，按以上设计思想设计的数据库满足三大范式的要求。
- 满足项目要求文档所要求的其它详细注意点，如外键无环、Unique约束列等。

### Task 3: Data Import

#### Task 3.1 Basic Requirements:

| 脚本名称                 | 作者          | 描述                                                         |
| ------------------------ | ------------- | ------------------------------------------------------------ |
| CSVFormatAdjustment.java | 陈明志&邱天润 | 数据筛选与格式调整调整。                                     |
| CSVReader.java           | 陈明志        | 通过运行这个Java脚本可以将全部数据分割为8个txt文件作为中间文件，分别对应数据库设计的8个表格。 |
| SQLGenerator.java        | 陈明志        | 将Resources中的8个txt文件作为输入，运行该脚本可以得到所有的建表语句以及插入内容的sql文件 |
| Loader.java              | 邱天润        | 运行这个Java脚本可以导入所有的数据到数据库中                 |
| FullLoader_MySQL.java    | 邱天润        | 运行这个Java脚本可以导入所有的数据到MySQL数据库中            |

在处理数据的过程中，我们通过创造了中间文件的方式来处理数据。

我们首先使用CSVFormatAdjustment.java对数据进行调整，把原有数据中Supply Center 名为 “Hong Kong, Macao and Taiwan regions of China” 改为 “Hong Kong and Macao and Taiwan regions of China” 以便后续数据导入。

然后我们使用CSVReader.java从原有csv文件中按行读入数据，以 “,” 分割，从而获得每个instance的具体信息。然后我们按建表语句分别将这些信息写入对应的txt文件，从而获得每个表对应的具体信息。

然后我们使用SQLGenerator.java分别读取Resources中的8个txt文件，生成对应的sql插入语句。生成的Data.sql中即包含导入数据的全部建表语句。

然后可以使用Loader.java从Data.sql中读入sql插入语句并执行，从而将全部数据导入数据库。由于Loader.java使用普通 `Statement` 逐条执行 SQL 文件中的完整插入语句，因此效率低下。后续3.3中我们会给出更高效的插入方式。

#### Task 3.2 Data Accuracy checking

##### Q1. How many salesmans are there for each gender?

```postgresql
select gender, count(*)
from salesman
group by gender;
```

![](https://github.com/renmiamu/CS307_Principles_of_Database_System_Project1/blob/main/Photos/Q1.png)

##### Q2. How many companies are there in each supply center?

```postgresql
select Sc.supply_center, count(company_id)
from Enterprise e
         join Supply_center Sc on Sc.supply_center = e.supply_center
group by Sc.supply_center;
```

![](https://github.com/renmiamu/CS307_Principles_of_Database_System_Project1/blob/main/Photos/Q2.png)

##### Q3. How many salesmans are there in each supply center?

```postgresql
select s.supply_center, count(salesman_id)
from salesman s
group by s.supply_center;
```

![](https://github.com/renmiamu/CS307_Principles_of_Database_System_Project1/blob/main/Photos/Q3.png)

##### Q4. How many salesmans are there within a given age range (lower and upper bounds)?  In this case: (30--40)

```postgresql
select count(*)
from salesman
where age > 30
  and age <= 40;
```

![](https://github.com/renmiamu/CS307_Principles_of_Database_System_Project1/blob/main/Photos/Q4_1.png)

##### Q5. How many countries are there in each supply center?

```postgresql
select supply_center, count(distinct r.country) as country
from region r
         join Enterprise E on r.region_id = E.region_id
group by supply_center;
```

![](https://github.com/renmiamu/CS307_Principles_of_Database_System_Project1/blob/main/Photos/Q5.png)

##### Q6. Given a country name (Italy or Canada), list all company names and their respective industries.

```postgresql
select country, client_enterprise, industry
from Enterprise e
         join Region R on R.region_id = e.region_id
where country = 'Italy'
   or country = 'Canada';
```

![](https://github.com/renmiamu/CS307_Principles_of_Database_System_Project1/blob/main/Photos/Q6_1.png)

##### Q7. Given a product code (L8N0649), list all product models and their corresponding unit prices.

```postgresql
select product_code, product_model_name, unit_price
from product_models
where product_code = 'L8N0649';
```

![](https://github.com/renmiamu/CS307_Principles_of_Database_System_Project1/blob/main/Photos/Q7_1.png)

##### Q8. Given a contract number (CSE0000003), list all order details in the contract, including (product model, order quantity, salesmans name, and lodgement_date).

```postgresql
select contract_number, product_model_name, quantity, salesman_name, lodgement_date
from contract c
         join Contract_details Cd on c.contract_number = Cd.contract_id
         join Product_models Pm on Pm.model_id = Cd.model_id
         join Salesman S on S.salesman_id = Cd.salesman_id
where contract_number = 'CSE0000003';
```

![](https://github.com/renmiamu/CS307_Principles_of_Database_System_Project1/blob/main/Photos/Q8_1.png)

#### Task 3.3 Advanced requirements

###  
