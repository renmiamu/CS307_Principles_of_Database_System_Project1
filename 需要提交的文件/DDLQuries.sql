--How many salesmans are there for each gender?
select gender, count(*)
from salesman
group by gender;

-- How many companies are there in each supply center?
select Sc.supply_center, count(company_id)
from Enterprise e
         join Supply_center Sc on Sc.supply_center = e.supply_center
group by Sc.supply_center;

--How many salesmans are there in each supply center?
select s.supply_center, count(salesman_id)
from salesman s
group by s.supply_center;

--How many salesmans are there within a given age range (lower and upper bounds)? (30--40)
select count(*)
from salesman
where age > 30
  and age <= 40;

--How many countries are there in each supply center?
select supply_center, count(distinct r.country) as country
from region r
         join Enterprise E on r.region_id = E.region_id
group by supply_center;

--Given a country name (Italy or Canada), list all company names and their respective industries.
select country, client_enterprise, industry
from Enterprise e
         join Region R on R.region_id = e.region_id
where country = 'Italy'
   or country = 'Canada';

--Given a product code, list all product models and their corresponding unit prices.
select product_code, product_model_name, unit_price
from product_models
where product_code = 'L8N0649';

--Given a contract number, list all order details in the contract,
-- including (product model, order quantity, salesmans name, and lodgement_date).
select contract_number, product_model_name, quantity, salesman_name, lodgement_date
from contract c
         join Contract_details Cd on c.contract_number = Cd.contract_id
         join Product_models Pm on Pm.model_id = Cd.model_id
         join Salesman S on S.salesman_id = Cd.salesman_id
where contract_number = 'CSE0000003';