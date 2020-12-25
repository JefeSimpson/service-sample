BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "client" (
	"id"	INTEGER,
	"first_name"	VARCHAR,
	"last_name"	VARCHAR,
	"email"	VARCHAR,
	"phone"	VARCHAR,
	"login"	VARCHAR,
	"password"	VARCHAR,
	"token"	VARCHAR,
	"destruction_time"	BLOB,
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "employee" (
	"id"	INTEGER,
	"first_name"	VARCHAR,
	"last_name"	VARCHAR,
	"email"	VARCHAR,
	"phone"	VARCHAR,
	"department"	VARCHAR,
	"login"	VARCHAR,
	"password"	VARCHAR,
	"token"	VARCHAR,
	"destruction_time"	BLOB,
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "order" (
	"id"	INTEGER,
	"client_id"	INTEGER,
	"product_id"	INTEGER,
	"order_date"	BLOB,
	"address"	VARCHAR,
	"quantity"	VARCHAR,
	"price"	VARCHAR,
	"status"	VARCHAR,
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "product" (
	"id"	INTEGER,
	"product_name"	VARCHAR,
	"description"	VARCHAR,
	"employee_id"	INTEGER,
	PRIMARY KEY("id" AUTOINCREMENT)
);
INSERT INTO "client" VALUES (1,'john','doe','j.doe@gmail.com','87213451242','john123','$2a$10$EGFY6VX.pFlF24Hl3G6pC.qUnXJKilxeRNL51y0NuOc3lhEbMvRoS','GWVKWQQUMJ3RAM3TJ3APBVWXHKXH4IQH',X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e5030f78');
INSERT INTO "employee" VALUES (1,'mark','henry','henry.m@gmail.com','87075071945','MANAGEMENT','henry.m','$2a$10$DLfpq1PNzbS14DKWcmMqQuQj8PN/PBUz8tFKmMDRDslXE9b3tfga.','F62AJNO3EGKWDSXYQQO474CCXQ2RVL7B',X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e5011b78');
INSERT INTO "employee" VALUES (2,'sam','walker','s.walker@gmail.com','87075072045','PRODUCTION','walker.s','$2a$10$/T3OccAsvoAAWLaaFVaaYOohd/Z7mWSBUZfwgE5sTZdydpMu9YO2m',NULL,NULL);
INSERT INTO "employee" VALUES (3,'din','bryan','d.bryan@gmail.com','87075071745','SALES','bryan.d','$2a$10$dBWoxuHg6EpDnUofBKSYuexprPhamUHU4TRdaKa5iRVPhWtkdVxpm',NULL,NULL);
INSERT INTO "order" VALUES (1,1,1,X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e40c1978','city 5','3','200000 tenge','IN_PROGRESS');
INSERT INTO "product" VALUES (1,'car','nice car',2);
COMMIT;
