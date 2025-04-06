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

![ER_Diagram](https://github.com/renmiamu/CS307_Principles_of_Database_System_Project1/blob/main/er_diagram.png)

### Task 2: Relational Database Design

本项目使用 [`DDL.sql`](https://github.com/renmiamu/CS307_Principles_of_Database_System_Project1/blob/main/DDL.sql) 文件创建数据表，使用 `PostgreSQL`的 `DDL` 语法编写。（后续在MySQL导入中，使用`MySQL`的语法改写进行导入。）

#### 数据库设计

使用 [`DataGrip`](https://www.jetbrains.com/datagrip/) 创建数据表并全选后通过右键 `Diagram > Show Diagram` 显示如下数据表设计及关系。

![Diagram](https://github.com/renmiamu/CS307_Principles_of_Database_System_Project1/blob/main/TABELS.png)

#### 设计思路及说明

##### 数据表及其各列含义说明

在整个项目中共创建了 11 个数据表，数据表和其中各列、外键的含义如下：

1. Enterprise 表存储公司的信息。包括公司id company_id（自增主键），客户公司 client_enterprise，公司所属的行业 industry。
2. Region 表存储地区信息。包括城市id city_id（自增主键）、国家 country 、城市名 city。
3. Products 表存储产品信息。包括产品编码 product_code（主键）、产品编码对应的产品名称 product_name。
4. Supply_center 表存储供应中心信息。包括供应中心 supply_center（主键）、所属区域的管理员 director。
5. Product_models 表存储产品型号的相关信息。包括产品型号id model_id（自增主键）、产品型号 product_model_name、产品型号对应的单价 unit_price。
6. Salesman 表存储销售员的信息。包括销售员编号 salesman_number、销售员姓名 salesman_name、销售员性别 gender、销售员年龄 age、销售员电话 mobile_number。
7. Enterprise_region 表建立公司和地区之间的联系。包括公司id company_id（主键，外键，参考Enterprise.company_id）、城市id city_id（外键，参考Region.city_id）。
8. Enterprise_product 表建立公司和产品之间的联系。包括公司id company_id（外键，参考Enterprise.company_id）、产品编码 product_code（外键，参考Products.product_code）、合同编号 contract_number、合同日期 contract_date。这个表用于记录公司与产品的具体合同信息，如公司销售或采购的产品及其合同细节。
9. Enterprise_supply_center 表建立公司和供应中心之间的联系。包括公司id company_id（主键，外键，参考Enterprise.company_id）、供应中心 supply_center（外键，参考Supply_center.supply_center）。
10. Product_models_product 表建立产品和产品型号之间的联系。包括产品编码 product_code（外键，参考Products.product_code）、产品型号id model_id（主键，外键，参考Product_models.model_id）。这个表用于描述哪些产品有具体型号，是连接产品和产品型号的桥梁。
11. Product_model_salesman 表记录销售员销售的产品型号信息。包括产品型号id model_id（外键，参考Product_models.model_id）、销售员编号 salesman_number（外键，参考Salesman.salesman_number）、销售数量 quantity、预计交货日期 estimated_deliver_date、登记日期 lodgement_date。这个表用于记录销售员销售的具体产品型号及相关销售信息。



