DROP DATABASE IF EXISTS `OWASP`;
CREATE DATABASE `OWASP`; 
USE `OWASP`;

-- Database for OWASP Juice Shop
-- Programmer: Seyedmehrad Adimi

CREATE TABLE `Employee` (
  `Employee ID` INT NOT NULL,
  `Employee Name` VARCHAR(40) NOT NULL,
  `Sex` VARCHAR(1) NOT NULL,
  `Birth Date`DATE NOT NULL,
  `Pay Rate` FLOAT NOT NULL,
  `Start Date` DATE NOT NULL,
  `Super ID` INT,
  `Warehouse ID` INT ,
  PRIMARY KEY (`Employee ID`)
);

CREATE TABLE `Warehouse`  (
  `Warehouse ID` INT NOT NULL,
  `Location` VARCHAR(40) NOT NULL,
  `Capacity` INT NOT NULL,
  `Number of Employees` INT NOT NULL,
  `Manager ID` INT,
  FOREIGN KEY (`Manager ID`) REFERENCES `Employee`(`Employee ID`),
  PRIMARY KEY (`Warehouse ID`)
);

ALTER TABLE Employee
ADD FOREIGN KEY (`Warehouse ID`) REFERENCES `Warehouse`(`Warehouse ID`) ON DELETE SET NULL;

ALTER TABLE Employee
ADD FOREIGN KEY (`Super ID`) REFERENCES `Employee`(`Employee ID`);

# BOSS
INSERT INTO `Employee` VALUES (100,'Zack Brown','M','1990-02-15',31.5,'2010-02-15',NULL, NULL);

# Warehouse Managers
INSERT INTO `Employee` VALUES (101,'Jeff Mcdonalds','M','1995-01-05',28.5,'2015-03-11',100, NULL);
INSERT INTO `Employee` VALUES (102,'Mark Zafron','M','1999-02-15',28.5,'2020-02-15',100, NULL);
INSERT INTO `Employee` VALUES (103,'Sophie Lawrens','F','1998-08-08',28.5,'2014-06-12',100, NULL);
INSERT INTO `Employee` VALUES (104,'Shawerma Eater','M','1997-01-29',28.5,'2020-03-24',100, NULL);

# Warehouse Workers
INSERT INTO `Employee` VALUES (105,'Jessica Walmart','F','2000-12-15',28.5,'2020-02-15',100, NULL);
INSERT INTO `Employee` VALUES (106,'Simon Ducks','M','1999-01-21',20,'2021-05-10',101, NULL);
INSERT INTO `Employee` VALUES (107,'Lana Jeffron','M','2001-02-11',20,'2021-06-15',101, NULL);
INSERT INTO `Employee` VALUES (108,'Kiana Roberts','F','2000-05-14',21.5,'2021-07-01',101, NULL);

INSERT INTO `Employee` VALUES (109,'Sima Shah','F','2000-12-15',28.5,'2020-02-15',100, NULL);
INSERT INTO `Employee` VALUES (110,'Shaban Robinson','M','1999-01-21',20,'2021-05-10',101, NULL);
INSERT INTO `Employee` VALUES (111,'Kyle Edinson','M','2001-02-11',20,'2021-06-15',101, NULL);
INSERT INTO `Employee` VALUES (112,'Eddie Zuckers','M','2000-05-14',21.5,'2021-07-01',101, NULL);

INSERT INTO `Employee` VALUES (113,'Clair Radmans','F','2000-12-15',28.5,'2020-02-15',100, NULL);
INSERT INTO `Employee` VALUES (114,'Samuel Zuckerberg','M','1999-01-21',20,'2021-05-10',101, NULL);
INSERT INTO `Employee` VALUES (115,'Jeffery Rockman','M','2001-02-11',20,'2021-06-15',101, NULL);
INSERT INTO `Employee` VALUES (116,'Burger Eater','M','2000-05-14',21.5,'2021-07-01',101, NULL);

INSERT INTO `Employee` VALUES (117,'Elsie Zieberth','F','2000-12-15',28.5,'2020-02-15',100, NULL);
INSERT INTO `Employee` VALUES (118,'Alex Markazof','M','1999-01-21',20,'2021-05-10',101, NULL);
INSERT INTO `Employee` VALUES (119,'Simon Daniels','M','2001-02-11',20,'2021-06-15',101, NULL);
INSERT INTO `Employee` VALUES (120,'Mehment Osrof','M','2000-05-14',21.5,'2021-07-01',101, NULL);


# Warehouses
INSERT INTO `Warehouse` VALUES (1,'Victoria', 1000,10,101);
INSERT INTO `Warehouse` VALUES (2,'Victoria', 2000,10,102);
INSERT INTO `Warehouse` VALUES (3,'Victoria', 1400,10,103);
INSERT INTO `Warehouse` VALUES (4,'Victoria', 1500,10,104);

# Update Warehouse Managers
UPDATE Employee SET `Warehouse ID` = 1 WHERE `Employee ID` = 101;
UPDATE Employee SET `Warehouse ID` = 2 WHERE `Employee ID` = 102;
UPDATE Employee SET `Warehouse ID` = 3 WHERE `Employee ID` = 103;
UPDATE Employee SET `Warehouse ID` = 4 WHERE `Employee ID` = 104;

# Update Warehouse Workers
UPDATE Employee SET `Warehouse ID` = 1 WHERE `Employee ID` = 105;
UPDATE Employee SET `Warehouse ID` = 2 WHERE `Employee ID` = 106;
UPDATE Employee SET `Warehouse ID` = 3 WHERE `Employee ID` = 107;
UPDATE Employee SET `Warehouse ID` = 4 WHERE `Employee ID` = 108;
UPDATE Employee SET `Warehouse ID` = 1 WHERE `Employee ID` = 109;
UPDATE Employee SET `Warehouse ID` = 2 WHERE `Employee ID` = 110;
UPDATE Employee SET `Warehouse ID` = 3 WHERE `Employee ID` = 111;
UPDATE Employee SET `Warehouse ID` = 4 WHERE `Employee ID` = 112;
UPDATE Employee SET `Warehouse ID` = 1 WHERE `Employee ID` = 113;
UPDATE Employee SET `Warehouse ID` = 2 WHERE `Employee ID` = 114;
UPDATE Employee SET `Warehouse ID` = 3 WHERE `Employee ID` = 115;
UPDATE Employee SET `Warehouse ID` = 4 WHERE `Employee ID` = 116;
UPDATE Employee SET `Warehouse ID` = 1 WHERE `Employee ID` = 117;
UPDATE Employee SET `Warehouse ID` = 2 WHERE `Employee ID` = 118;
UPDATE Employee SET `Warehouse ID` = 3 WHERE `Employee ID` = 119;
UPDATE Employee SET `Warehouse ID` = 4 WHERE `Employee ID` = 120;




CREATE TABLE `Client` (
  `Client ID` INT,
  `Client Name` VARCHAR(40) NOT NULL,
  `Username` VARCHAR(40) NOT NULL,
  `Number of Purchases` INT NOT NULL,
  `Warehouse ID` INT NOT NULL,
  FOREIGN KEY (`Warehouse ID`) REFERENCES `Warehouse`(`Warehouse ID`),
  PRIMARY KEY (`Client ID`)
);



INSERT INTO `Client` VALUES (1,'Mehrad Adimi','Meri900',10,1);
INSERT INTO `Client` VALUES (2,'Salam Fazil','salaamFazil',11,2);
INSERT INTO `Client` VALUES (3,'Kyle Sullivan','Kyle123',12,3);
INSERT INTO `Client` VALUES (4,'Nicole Makarowski','NicMaka',14,4);
INSERT INTO `Client` VALUES (5,'Ewan Morgan','EwanMori',0,1);



CREATE TABLE `Product` (
  `Product ID` INT,
  `Product Name` VARCHAR(60),
  `Product Price` FLOAT,
  `Description` VARCHAR(400),
  `Number of Reviews` INT,
  `Warehouse ID` INT,
  FOREIGN KEY (`Warehouse ID`) REFERENCES `Warehouse`(`Warehouse ID`),
  PRIMARY KEY (`Product ID`)
);

INSERT INTO `Product` VALUES (1,'Anniversary Ticket', 20,'Get your free ticket for OWASP 
20th Anniversary Celebration online conference!
 Hear from world renowned keynotes and special speakers,
 network with your peers and interact with our event sponsors.
 With an anticipated 10k+ attendees from around the world, 
 you will not want to miss this live on-line event!',0,1);

INSERT INTO `Product` VALUES (2,'Apple Juice', 2,'The all-time classic.',0,1);
INSERT INTO `Product` VALUES (3,'Apple Pomace', 0.89,'Finest pressings of apples. Allergy disclaimer: Might contain traces of worms. Can be sent back to us for recycling.',0,1);
INSERT INTO `Product` VALUES (4,'Banana Juice', 1.99,'Monkeys love it the most.',0,1);
INSERT INTO `Product` VALUES (5,'Art Work', 5000,'Unique digital painting depicting Stan, our most 
qualified and almost profitable salesman. He made a succesful carreer in selling used ships, 
coffins, krypts, crosses, real estate, life insurance, restaurant supplies, voodoo enhanced asbestos 
and courtroom souvenirs before finally adding his expertise to the Juice Shop marketing team.',0,2);
INSERT INTO `Product` VALUES (6,'Carrot Juice', 2.99,'As the old German saying goes: "Carrots are good for the eyes. Or has anyone ever seen a rabbit with glasses?"',0,1);
INSERT INTO `Product` VALUES (7,'Eggfruit Juice', 8.99,'Now with even more exotic flavour.',0,1);
INSERT INTO `Product` VALUES (8,'Fruit Press', 89.99,'Fruits go in. Juice comes out. Pomace you can send back to us for recycling purposes.',0,1);
INSERT INTO `Product` VALUES (9,'Green Smoothie', 1.99,'Looks poisonous but is actually very good for your health! Made from green cabbage, spinach, kiwi and grass.',0,3);
INSERT INTO `Product` VALUES (10,'Juice Shop "Permafrost" 2020 Edition', 9999.99,'Exact version of OWASP Juice Shop that was archived on 
02/02/2020 by the GitHub Archive Program and ultimately went into the Arctic Code Vault on July 8. 
2020 where it will be safely stored for at least 1000 years.',0,3);
INSERT INTO `Product` VALUES (11,'Lemon Juice', 2.99,'Sour but full of vitamins.',0,3);
INSERT INTO `Product` VALUES (12,'Melon Bike (Comeback-Product 2018 Edition)', 2999,'The wheels of this bicycle are made from real water melons. You might not want to ride it up/down the curb too hard.',0,1);
INSERT INTO `Product` VALUES (13,'King of the Hill Facemask', 13.49,'Facemask with compartment for filter from 50% cotton and 50% polyester.',0,4);
INSERT INTO `Product` VALUES (14,'CTF Girlie-Shirt', 22.49,'For serious Capture-the-Flag heroines only!',0,4);
INSERT INTO `Product` VALUES (15,'Juice Shop Card (non-foil)', 1000,'Mythic rare (obviously...) card "OWASP Juice Shop" with three distinctly useful abilities. 
Alpha printing, mint condition. A true collectors piece to own!',0,4);
INSERT INTO `Product` VALUES (16,'Juice Shop Coaster', 19.99,'Our 95mm circle coasters are printed in full color and made from thick, premium coaster board.',0,4);
INSERT INTO `Product` VALUES (17,'Holographic Sticker', 2,'Die-cut holographic sticker. Stand out from those 08/15-sticker-covered laptops with this shiny beacon of 80s coolness!',0,3);
INSERT INTO `Product` VALUES (18,'Juice Shop Hoodie', 49.99,'Mr. Robot-style apparel. But in black. And with logo.',0,2);
INSERT INTO `Product` VALUES (19,'Juice Shop Iron-Ons (16pcs)', 14.99,'Upgrade your clothes with washer safe iron-ons of the OWASP Juice Shop or CTF Extension logo!',0,2);
INSERT INTO `Product` VALUES (20,'Juice Shop Logo (3D-printed)', 99.99,'This rare item was designed and handcrafted in Sweden. 
This is why it is so incredibly expensive despite its complete lack of purpose.',0,2);
INSERT INTO `Product` VALUES (21,'Juice Shop Magnets (16pcs)', 15.99,'Your fridge will be even cooler with these OWASP Juice Shop or CTF Extension logo magnets!.',0,2);
INSERT INTO `Product` VALUES (22,'Juice Shop Mug', 21.99,'Black mug with regular logo on one side and CTF logo on the other! Your colleagues will envy you!',0,4);
INSERT INTO `Product` VALUES (23,'Juice Shop Sticker Page', 9.99,'Massive decoration opportunities with these OWASP Juice Shop or CTF Extension sticker pages! Each page has 16 stickers on it.',0,2);
INSERT INTO `Product` VALUES (24,'Juice Shop Sticker Single', 4.99,'Super high-quality vinyl sticker single with the OWASP Juice Shop or CTF Extension logo! The ultimate laptop decal!',0,1);



CREATE TABLE `Orders` (
  `Orders ID` INT NOT NULL,
  `Client ID` INT NOT NULL,
  `Ship Date`  DATE NOT NULL,
  `Product ID` INT,
  FOREIGN KEY (`Client ID`) REFERENCES `Client`(`Client ID`),
  FOREIGN KEY (`Product ID`) REFERENCES `Product`(`Product ID`),
  PRIMARY KEY (`Orders ID`)
);


INSERT INTO `Orders` VALUES (1,5, '2019-02-12',2);
INSERT INTO `Orders` VALUES (2,3, '2019-03-12',3);
INSERT INTO `Orders` VALUES (3,2, '2019-04-12',5);
INSERT INTO `Orders` VALUES (4,1, '2019-05-12',2);
INSERT INTO `Orders` VALUES (5,4, '2019-02-10',6);
INSERT INTO `Orders` VALUES (6,5, '2019-02-09',5);
INSERT INTO `Orders` VALUES (7,3, '2019-02-15',21);
INSERT INTO `Orders` VALUES (8,1, '2019-02-18',22);
INSERT INTO `Orders` VALUES (9,2, '2019-02-19',9);
INSERT INTO `Orders` VALUES (10,3, '2019-02-15',20);
INSERT INTO `Orders` VALUES (11,1, '2019-02-18',15);
INSERT INTO `Orders` VALUES (12,2, '2019-02-19',6);
INSERT INTO `Orders` VALUES (13,2, '2019-02-19',14);
INSERT INTO `Orders` VALUES (14,3, '2019-02-15',11);
INSERT INTO `Orders` VALUES (15,1, '2019-02-18',10);
INSERT INTO `Orders` VALUES (16,2, '2019-02-19',4);
INSERT INTO `Orders` VALUES (17,5, '2019-02-09',5);
INSERT INTO `Orders` VALUES (18,3, '2019-02-15',16);
INSERT INTO `Orders` VALUES (19,1, '2019-02-18',17);
INSERT INTO `Orders` VALUES (20,2, '2019-02-19',18);
INSERT INTO `Orders` VALUES (21,2, '2019-02-19',7);
INSERT INTO `Orders` VALUES (22,5, '2019-02-09',9);
INSERT INTO `Orders` VALUES (23,3, '2019-02-15',22);
INSERT INTO `Orders` VALUES (24,1, '2019-02-18',18);
INSERT INTO `Orders` VALUES (25,2, '2019-02-19',5);




CREATE TABLE `Supplier` (
  `Supplier ID` INT,
  `Supplier Name` VARCHAR(40),
  `Type` VARCHAR(40),
  `Warehouse ID` INT,
  FOREIGN KEY (`Warehouse ID`) REFERENCES `Warehouse`(`Warehouse ID`),
  PRIMARY KEY (`Supplier ID`)
);

INSERT INTO `Supplier` VALUES (1,'BB Foods', 'Fruits',1);
INSERT INTO `Supplier` VALUES (2,'Sigma Woods', 'Wood',2);
INSERT INTO `Supplier` VALUES (3,'Paintman', 'Paint',3);
INSERT INTO `Supplier` VALUES (4,'Ticket Leader', 'Tickets',4);




CREATE TABLE `Manages` (
  `Employee ID` INT,
  `Warehouse ID` INT,
  `Total Managment Years` INT,
  PRIMARY KEY (`Employee ID`, `Warehouse ID`),
  FOREIGN KEY (`Warehouse ID`) REFERENCES `Warehouse`(`Warehouse ID`) ON DELETE CASCADE,
  FOREIGN KEY (`Employee ID`) REFERENCES `Employee`(`Employee ID`)ON DELETE CASCADE
);
INSERT INTO `Manages` VALUES (101,1,2);
INSERT INTO `Manages` VALUES (102,2,1);
INSERT INTO `Manages` VALUES (103,3,1);
INSERT INTO `Manages` VALUES (104,4,1);



CREATE TABLE `Works At` (
  `Employee ID` INT,
  `Warehouse ID` INT,
  `Years of Working` FLOAT,
  PRIMARY KEY (`Employee ID`, `Warehouse ID`),
  FOREIGN KEY (`Warehouse ID`) REFERENCES `Warehouse`(`Warehouse ID`) ON DELETE CASCADE,
  FOREIGN KEY (`Employee ID`) REFERENCES `Employee`(`Employee ID`)ON DELETE CASCADE
);

DELIMITER $$
CREATE PROCEDURE LoadWorksAt()
BLOCK: BEGIN
DECLARE i INT;
DECLARE j INT;
DECLARE Numbers INT;
SET i = 1;
SET j = 1;
SET Numbers = 15;

WHILE (j <= Numbers+1) DO
	INSERT INTO `Works At` VALUES (104+j,i,(DATEDIFF(CURDATE(),(SELECT `Start Date` from Employee WHERE `Employee ID` = 104+j))/365));
    SET i = i+1;
    SET j = j+1;
    IF i > 4 THEN SET i=1; END IF;
    END WHILE;
END $$
DELIMITER ;
CALL LoadWorksAt();


CREATE TABLE `Stores` (
  `Warehouse ID` INT,
  `Product ID` INT,
  `Number of Product Stored` INT,
  PRIMARY KEY (`Warehouse ID`, `Product ID`),
  FOREIGN KEY (`Warehouse ID`) REFERENCES `Warehouse`(`Warehouse ID`)ON DELETE CASCADE,
  FOREIGN KEY (`Product ID`) REFERENCES `Product`(`Product ID`)ON DELETE CASCADE
);
INSERT INTO `Stores` VALUES (1,1,1000);
INSERT INTO `Stores` VALUES (1,2,1400);
INSERT INTO `Stores` VALUES (1,3,1500);
INSERT INTO `Stores` VALUES (1,4,1200);
INSERT INTO `Stores` VALUES (1,6,1600);
INSERT INTO `Stores` VALUES (1,7,2000);
INSERT INTO `Stores` VALUES (1,8,150);
INSERT INTO `Stores` VALUES (1,12,1111);
INSERT INTO `Stores` VALUES (1,24,1654);


INSERT INTO `Stores` VALUES (2,5,1000);
INSERT INTO `Stores` VALUES (2,18,1400);
INSERT INTO `Stores` VALUES (2,19,1500);
INSERT INTO `Stores` VALUES (2,20,1200);
INSERT INTO `Stores` VALUES (2,21,1600);
INSERT INTO `Stores` VALUES (2,23,2000);

INSERT INTO `Stores` VALUES (3,9,1500);
INSERT INTO `Stores` VALUES (3,10,1200);
INSERT INTO `Stores` VALUES (3,11,1600);
INSERT INTO `Stores` VALUES (3,17,2000);

INSERT INTO `Stores` VALUES (4,13,2000);
INSERT INTO `Stores` VALUES (4,14,1500);
INSERT INTO `Stores` VALUES (4,15,1200);
INSERT INTO `Stores` VALUES (4,16,1600);
INSERT INTO `Stores` VALUES (4,22,2000);





-- SELECT COLUMN_NAME
-- FROM INFORMATION_SCHEMA.COLUMNS
-- WHERE TABLE_SCHEMA = 'OWASP'
--   AND TABLE_NAME = 'Warehouse'
--   AND COLUMN_KEY = 'PRI';
--   

SELECT COLUMN_NAME
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'OWASP'
AND TABLE_NAME = 'Warehouse'
AND COLUMN_KEY = 'MUL';



-- select * from INFORMATION_SCHEMA.Columns
-- WHERE TABLE_SCHEMA = 'OWASP'




