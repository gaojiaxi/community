# Introduction
This project is a reimplementation of Nowcoder.community.com.
To be more specific, Front-end is implemented HTML/CSS/Javascript. 
I used thymeleaf as the template engine as it is a modern server-side Java template engine for both web and standalone environments.
The back-end is implemented using Spring Framework and Java. 
Gnerally, it supports the following functions:
* User can Login/Registration
* User can post posts to the platform.
* User can like/comment other users' post.
* User can send private letter to other users.
* User can follow other users.
* User will get notification when other users comment/like/follow his posts.
* System will automatically fillter inappropriate content.
* User can search posts using keywords. 

## Tech Stack
* Java (backend)/HTML/CSS/Javascript (frontend)
* SpringMVC/Apache Maven/Spring Email/Mybatis/MySQL/Redis/Apache Kafka/ElasticSearch/AJAX
* AWS cloud Computing/AWS EC2

## Project Structure
The overall structure (tech stack) of this project:
![](https://github.com/gaojiaxi/Around/blob/master/demoPics/structure.png)
![](https://github.com/gaojiaxi/Around/blob/master/demoPics/dataflow.png)


## Reasons for using such tech stacks <br>
**1. Why using IntelliJ as the IDE?**<br>
* IntelliJ is the most popular Java IDE.
* IntelliJ has better code autocompletion and renaming suggestions.
* Intellij indexes the world and everything just works intuitively.
* I have used eclipse for more than one year and I felt IntelliJ is much easier to use compared to eclipse.

**2. Why using Mybatis instead of plain JDBC?**<br>
* Out-of-the-box table/query caching.
* Dynamic SQL.
* SQL is stored outside of the code.
* Templating SQL for easier DB vendor Independence.

**3. What is Redis?**<br>
* Redis is a NoSQL database based on key-value pairs.
* Redis stored data in memory, which makes it very fast.
* Redis also stored data in local disk which makes it very safe and easy to restore from failure.
* Redis can be used for:Caching, Ranking, Counting, Social Network and Message queue.

**3. Why using Redis when you already have mysql?**<br>
* I used redis to implement like/follow method. (i.e., user can like a post/comment, user can follow other users).
* I used redis to implement count like/follow method(i.e., count how many followers and how many likes that a use has received)
* I used redis to refactoring login module. To be more specific, I applied redis in three parts:
    * Using Redis to store kaptcha.
        * Since kaptcha are frequently visited and refreshed, it requires high performance.
        * Since kaptcha are only valid for a short period of time.
    * Using Redis to store login Ticket
        * Previously, I stored loginTicket in MYSQL, whenever user make a request, The server need to search in the SQL to verify the user.
        * Storing login ticket in SQL will become slow when more and more users used our website. 
        * Now, I stored loginTicket in Redis since redis is fast. Also we do not have to keep loginTicket forever since the user will go offline after a period of time.
    * Using Redis to Store User Information
        * Previously, I stored User information in MYSQL, whenever user make a request, The server need to search in the SQL to obtain Userinformation.
        * Storing user information in SQL will become slow when more and more users used our website. 
        * Now I am using Redis to cache user information to improve server respond time. 

**4. What is BlockingQueue and Why we need BlockingQueue?**<br>
* A blocking queue is a queue that blocks when you try to dequeue from it and the queue is empty, or if you try to enqueue items to it and the queue is already full. 
A thread trying to dequeue from an empty queue is blocked until some other thread inserts an item into the queue. A thread trying to enqueue an item in a full queue is blocked until some other thread makes space in the queue, either by dequeuing one or more items or clearing the queue completely.
A blocking queue is illustrated as follows.
![](https://github.com/gaojiaxi/community/blob/master/demoPics/BlockingQueue.png)
* Producer/Consumer design pattern
    * Producer: Thread which produce data
    * Consumer: Thread which consume data
* BlockingQueue can be used to solve Thread Communication problems caused by imblanced speed(e.g., Producer thread produce data much faster than Consumer Thread)
* Implented Class:
    * ArrayBlockingQueue
    * LinkedBlockingQueue
    * PriorityBlockingQueue
    * SynchronousQueue
    * DelayQueue

**5. What is Kafka?**<br>
* Apache Kafka is an event streaming platform.
* Kafka can be applied for Message Service, log data collection, User activity tracking, etc.
* Kafka has High throughput, persistent data, high reliability and high extensibility.
* Kafka key concept:
    * Broker(server), Zookeeper(Centralized service which monitoring distributed systems)
    * Topic, Partition, Offset
    * Leader Replica, Follow Replica
* A kafka demo is illustrated as follows:
![](https://github.com/gaojiaxi/community/blob/master/demoPics/Kafka_structure.png)

**6. How to use Kafka in our project?**<br>
For our project, there are three types(topics) of messages:
* Comment: triggered when other users make a comment on a posts/comments.
* Like: triggered when other users likes a posts/comments.
* Follow: triggered when users follows other users.

As illustrated below: 
![](https://github.com/gaojiaxi/community/blob/master/demoPics/Kafka_message_topics.png)

We can use kafkaTemplate to define producer and consumer:
* Producer:
```
kafkaTemplate.send(topic, data);
```
* Listener:
```
@KafkaListener(topics = {"test"})
public void handleMessage(ConsumerRecord record) {}
```
**7. What is elastic Search?**<br>

## MySQL install and initialization
1. Download [mysql](https://dev.mysql.com/downloads/mysql) and [mysql-workbench](https://dev.mysql.com/downloads/workbench)
2. Unzip both to a specific folder(e.g. F://workspace//mysql, F://workspace//mysql-workbench)
3. Copy "./community-init-sql-1.5/my.ini" to your mysql root directory, you may have to change port number or basedir based on your PC.
4. Add mysql root directory to System Path.
5. Run cmd, change the current diretory to mysql/root/bin
6. Initialize mysql using following code, copy the temporary password.
    ```
    mysql1d --initilize --console
    ```
7. Install mysql service
    ```
    mysql1d install
    ```
8. Start mysql Service
   ```
   net start mysql
   ```
9. Open a new terminal, access mysql using following code and temporary password obtained from step 6.
    ```
    mysql -uroot -p
    ```
10. Change temporary password to yourpassword 
    ```
    alter user root@localhost identified by "yourpassword"
    ```
11. Create a database for this project, then use it
    ```
    create database community
    use community:
    ```
12. Create table using init_schema.sql
    ```
    source F:/work/community-init-sql/init_schema.sql;
    ```
13. Insert test data using init_data.sql
    ```
    source F:/work/community-init-sql/init_data.sql;
    ```
14. Test if the test is inserted into mysql table
    ```
    select * from user
    ```
    you should be able to see
    ![](https://github.com/gaojiaxi/community/blob/master/demoPics/mysql_init_test.png)


## ElasticSearch install and configuration
* Download [Elastic Search](https://www.elastic.co/cn/downloads/past-releases/elasticsearch-6-4-3) and [elasic-ik](https://github.com/medcl/elasticsearch-analysis-ik/releases/tag/v6.4.3) from the link.
Note that due to dramatic API change between ES6 to ES7, this project this tested only on ES 6.4.3 and ES-IK 6.4.3. I may refactor the code to adapt to ES newest version when I have time.
* Unzip ES to your working directory and Unzip ES-IK to ES-root/pligins.
* Open ES-root/config/elasticsearch.yml, change cluster.name to your project name and change path.data and path.logs to your working directory.
* Open terminal, run ES-root/bin/elasticsearch.bat.If ES is installed correctly, you should able to see 
 ![](https://github.com/gaojiaxi/community/blob/master/demoPics/es_run.png)
* Open a new terminal, check if es is running correctly using the following commands.
```
-X GET "localhost:9200/_cat/health?v"
-X GET "localhost:9200/_cat/nodes?v"
```
you should see:
 ![](https://github.com/gaojiaxi/community/blob/master/demoPics/es_run_check.png)



## Requirements
* Java 8.0
* Mysql
* Mybatis
* Spring framework
* Redis
* Apache Kafka
* Elastic Search 6.4.3
* Postman (optional)


## Installation

## Usage/Quick Start

## Screenshots

## Known issues

## Todo list
* Front-end implementation using React.js.

## Version Change Log

## Licenses
NAN

## Notes
If you have any questions, feel free to contact me at jiaxig@ece.ubc.ca

