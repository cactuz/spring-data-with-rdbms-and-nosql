# Reviews API 
An API using Springboot with two databases for persistence: an RDBMS and a NoSQL.
The API supports operations for insert and retrieval with nested relationships.

The API story includes the creation of products, each with a unique product id.
Then reviews may be posted for an existing product.
Finally comments may be added for each review.

### Prerequisites
MySQL and MongoDB needs to be installed and configured. 
Please create a database in your local machine. 
Then set the following properties in application.properties file:
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=

You can install mongo using docker and set 
spring.data.mongodb.uri=

Each time you execute a successful post, the data will be persisted. 
There may be instances where you want to start with clean data and for that please drop and 
recreate the mysql and mongodb databases.

### API endpoints and Guides
You can use the end point and sample http requests in src/test/http

