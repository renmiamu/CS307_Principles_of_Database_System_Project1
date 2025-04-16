# CS307 Database Project1
**成员：沈泓立（12311016），郑袭明（12311011）**

### 成员分工及贡献百分比

**沈泓立**：

- E-R图绘制
- Java数据筛选与导入
- Python和C++数据导入框架与编写
- Python和C++部分多线程优化以及disable trigger和entrigger的对比测试
- 多平台导入数据测试（Windows、macOS）
- 项目相关的accuracy checking SQL语句编写
- 项目报告写作

**郑袭明**：

- 数据库建表设计
- Java数据筛选与导入
- 基于Java的多种导入优化，如Batch、多线程等，并进行比较
- 不同的Data Volume测试优化
- 进行非Postgres导入测试（OpenGauss，MySQL）
- 项目报告写作

贡献百分比相同，均为**50%**

### Task 1: E-R Diagram

本小组使用 [drawio](https://www.diagrams.net/) 绘图工具，绘制本项目的 E-R 图，截图如下：

![](https://github.com/renmiamu/CS307_Principles_of_Database_System_Project1/blob/main/er_diagram.png)

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

