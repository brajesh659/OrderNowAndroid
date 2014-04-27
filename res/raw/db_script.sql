PRAGMA foreign_keys = false;

-- ----------------------------------------------------
--  Table structure for "currentOrders"
-- ----------------------------------------------------
DROP TABLE IF EXISTS "currentOrders";
CREATE TABLE [currentOrders] (
  [orderId] nvarchar(300) not null
, [restId] nvarchar(300) not null
, [tableId] nvarchar(300) not null
, [dishId] nvarchar(300) not null
, [qty] nvarchar(1000) not null
, [state] nvarchar(1000) null
, [timestamp] nvarchar(1000) not null
, primary key ([orderId],[restId])

);

INSERT INTO currentOrders VALUES('1','r1','t1','d1','1','Ordered','1231231231231231');


-- ----------------------------------------------------
--  Table structure for "previousOrders"
-- ----------------------------------------------------
DROP TABLE IF EXISTS "previousOrders";
CREATE TABLE [previousOrders] (
  [orderId] nvarchar(300) not null
, [restId] nvarchar(300) not null
, [tableId] nvarchar(300) not null
, [dishId] nvarchar(300) not null
, [qty] nvarchar(1000) not null
, [state] nvarchar(1000) null
, [timestamp] nvarchar(1000) not null
, primary key ([orderId],[restId])
);

-- ----------------------------------------------------
--  Table structure for "customerInfo"
-- ----------------------------------------------------
DROP TABLE IF EXISTS "customerInfo";
CREATE TABLE [customerInfo] (
  [name] nvarchar(300) not null
, [mobileNo] nvarchar(300) not null
, [emailId] nvarchar(300) not null
, primary key ([name])
);



-- ----------------------------------------------------
--  Table structure for "dishes"
-- ----------------------------------------------------
DROP TABLE IF EXISTS "dishes";
CREATE VIRTUAL TABLE [dishes] USING fts3(
  [dishId] nvarchar(300) not null
, [name] nvarchar(300) not null
, [description] nvarchar(300) not null
, [img] nvarchar(300) not null
, [price] nvarchar(300) not null
, [dishProperties] nvarchar(300) not null
, [category] nvarchar(300) not null
, [dishDetail] nvarchar(300) not null
);

-- ----------------------------------------------------
--  Table structure for "restaurants"
-- ----------------------------------------------------
DROP TABLE IF EXISTS "restaurants";
CREATE VIRTUAL TABLE [restaurants] USING fts3(
  [rId] nvarchar(300) not null
, [resValue] clob not null
, primary key ([rId])
);

CREATE TABLE IF NOT EXISTS[restaurantMenu] (
  [restId] nvarchar(300) not null
, [menu] clob not null
, primary key ([restId])
);
