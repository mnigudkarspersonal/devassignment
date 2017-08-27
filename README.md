## DEV Assignment
This is a spring boot application for rest API. This application uses JPA with hibernate for DB interaction. It implements CRUD operation for invoice and Items within the invoice.

### How to build and run the application
* Install and start mysql server.
* Check out the code.
* Create a database (create database dev_assignment). If the DB name is different then update spring.datasource.url property in  /resources/application.properties file. 
* cd into the top level directory where pom.xml resides and run "mvn clean install"
* To start spring boot service run "mvn spring-boot:run"
* Service creates 2 tables, invoice and item, at start up.
* Comment out "spring.jpa.hibernate.ddl-auto=create" entry in /resources/application.properties file otherwise every service startup DB will be recreated and previous data will be lost.

### Models
* Invoice: invoiceId, name, email, and list of items.
* Item: itemId, description, and amount.

### API Description

#### Create Request

```
{
  "name": "test",
  "email": "testEmail1@gmail.com",
  "dueDate": "2017-08-26",
  "itemList": [
    {
      "description": "testItem1",
      "amount": 10
    },
    {
      "description": "testItem2",
      "amount": 13
    }
  ]
}

```

#### Create response

```
{
    "invoiceId": "cb4f0f0c-9aa4-43ce-ba1d-786c1acf5185",
    "name": "test",
    "email": "testEmail1@gmail.com",
    "dueDate": "2017-08-26",
    "itemList": [
        {
            "itemId": "e041dd31-a4a3-4f0e-9719-a88e76581fee",
            "description": "testItem1",
            "amount": "10"
        },
        {
            "itemId": "15040055-5d1e-4557-babc-d8134a4abc0e",
            "description": "testItem2",
            "amount": "13"
        }
    ],
    "totalAmount": 23,
    "deleted": false
}

```

#### Get request

```
{
  "invoiceId": "cb4f0f0c-9aa4-43ce-ba1d-786c1acf5185"
}

```

#### Get response

```

{
  "invoiceId": "cb4f0f0c-9aa4-43ce-ba1d-786c1acf5185",
  "name": "test",
  "email": "testEmail1@gmail.com",
  "dueDate": "2017-08-26",
  "itemList": [
    {
      "itemId": "e041dd31-a4a3-4f0e-9719-a88e76581fee",
      "description": "testItem1",
      "amount": "10"
    },
    {
      "itemId": "15040055-5d1e-4557-babc-d8134a4abc0e",
      "description": "testItem2",
      "amount": "13"
    }
  ],
  "totalAmount": 23,
  "deleted": false
}

```

#### Delete request

```
{
  "deleted": true,
  "invoiceId": "fab653e7-61f0-4394-8746-a377ef0f6dc3"
}
{code}

{code: title = Delete response}
{
  "invoiceId": "fab653e7-61f0-4394-8746-a377ef0f6dc3",
  "totalAmount": 0,
  "deleted": true
}

```

#### Update invoice request

```
{
  "invoiceId": "7dfd56f8-85d5-4111-9458-33159d5b7344",
  "name": "test1",
  "email": "testEmail1@gmail.com",
  "dueDate": "2017-08-26"
}

```

#### Update invoice response

```
{
    "invoiceId": "7dfd56f8-85d5-4111-9458-33159d5b7344",
    "name": "test1",
    "email": "testEmail1@gmail.com",
    "dueDate": "2017-08-26",
    "deleted": false
}

```

#### Update item request

```
{
  "invoiceId": "7dfd56f8-85d5-4111-9458-33159d5b7344",
  "name": "test2",
  "email": "testEmail2@gmail.com",
  "dueDate": "2017-08-27",
  "itemList": [
        {
            "itemId": "287f7bef-60ef-412c-b2c8-0045be33a6c5",
            "description": "testItem3",
            "amount": "20"
        },
        {
            "itemId": "409a3ca1-8141-4a1e-af50-3a225a0102c1",
            "description": "testItem4",
            "amount": "33"
        }
    ]
}

```
#### Update item response

```
{
    "invoiceId": "7dfd56f8-85d5-4111-9458-33159d5b7344",
    "name": "test2",
    "email": "testEmail2@gmail.com",
    "dueDate": "2017-08-27",
    "itemList": [
        {
            "itemId": "287f7bef-60ef-412c-b2c8-0045be33a6c5",
            "description": "testItem3",
            "amount": "20"
        },
        {
            "itemId": "409a3ca1-8141-4a1e-af50-3a225a0102c1",
            "description": "testItem4",
            "amount": "33"
        }
    ],
    "deleted": false
}

```
