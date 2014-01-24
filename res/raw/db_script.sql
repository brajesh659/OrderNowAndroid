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