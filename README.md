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
* SpringMVC/Spring Email/Mybatis/MySQL/Redis/Apache Kafka/ElasticSearch/AJAX
* AWS cloud Computing/AWS EC2

## Project Structure
The overall structure (tech stack) of this project:
![](https://github.com/gaojiaxi/Around/blob/master/demoPics/structure.png)
![](https://github.com/gaojiaxi/Around/blob/master/demoPics/dataflow.png)

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
    ![](https://github.com/gaojiaxi/Around/blob/master/demoPics/dataflow.png)
## How to config and 
You can use any IDE for this project, I recommand using Goland or Vim.
Here is how I configured my VIM
* Install vim-go plugin for vim: https://github.com/fatih/vim-go
* Minimal vim configuration (~/.vim/vimrc, or $HOME/vimfiles/vimrc for Windows)
    ```
    set nocompatible
    set encoding=utf-8
    set autoread
    
    syntax on
    filetype indent plugin on
    
    set ruler
    
    “ If you use a dark background
    set background=dark
    ```

## How to configure and start elastic search engine in Google Compute Engine

1. step1: Open your console.cloud.google.com. Then Find NETWORKING -> VPC network -> Firewall rules.  
![](https://github.com/gaojiaxi/Around/blob/master/demoPics/config_es_step1_1.png)  
Click ‘CREATE FIREWALL RULE’  
![](https://github.com/gaojiaxi/Around/blob/master/demoPics/config_es_step1_2.png)  
In the next page, give it a name like ‘elasticsearch’.   
Set the Target tags to be ‘es’, source IP ranges to be ‘0.0.0.0/0’, and the specified protocols and ports to be ‘tcp:9200’.   
![](https://github.com/gaojiaxi/Around/blob/master/demoPics/config_es_step1_3.png)  
Wait until the firewall rules is created.   

2. step2: Find Compute Engine->VM instances  
![](https://github.com/gaojiaxi/Around/blob/master/demoPics/config_es_step2_1.png)  
Choose ‘Create’
![](https://github.com/gaojiaxi/Around/blob/master/demoPics/config_es_step2_2.png)  
Choose ‘Change’ and switch to Ubuntu 16. Keep the size of 10GB is fine.  
![](https://github.com/gaojiaxi/Around/blob/master/demoPics/config_es_step2_3.png)
![](https://github.com/gaojiaxi/Around/blob/master/demoPics/config_es_step2_4.png)  
Then in the Networking -> Network tags, set it to be ‘es’ (the firewall rule that we created).   
![](https://github.com/gaojiaxi/Around/blob/master/demoPics/config_es_step2_5.png)  

3. step3: After one minute, you will see the instance is started. Choose ‘SSH’ and then ‘Open in browser window’.
 ![](https://github.com/gaojiaxi/Around/blob/master/demoPics/config_es_step3_1.png)  
 ![](https://github.com/gaojiaxi/Around/blob/master/demoPics/config_es_step3_2.png)  
4. step4  
 In the terminal, enter
     ```
   sudo apt-get update
   sudo apt-get install default-jre
    ```  
   It will install java to your vm. To verify, enter ‘which java’ and ‘java -version’, you will see  
   ![](https://github.com/gaojiaxi/Around/blob/master/demoPics/config_es_step4_1.png)  
   Install ElasticSearch as below  
   ```
   wget https://download.elastic.co/elasticsearch/release/org/elasticsearch/distribution/deb/elasticsearch/2.3.1/elasticsearch-2.3.1.deb
   sudo dpkg -i elasticsearch-2.3.1.deb
   sudo systemctl enable elasticsearch.service
   ```
   Edit the configuration file  
   ```
   sudo vim /etc/elasticsearch/elasticsearch.yml
   ```
   Add two lines to the config, to allow all traffic and listen on port 9200.  
   ```
   network.host: 0.0.0.0
   http.port: 9200
   ```
   Save this and start ElasticSearch  
   ```
   sudo service elasticsearch start
   ```
   Check the status of ElasticSearch  
   ```
   sudo service elasticsearch status
   ```
   If the service started correctly, you will see ‘active’ in the status  
   ![](https://github.com/gaojiaxi/Around/blob/master/demoPics/config_es_step4_2.png) 


## Reasons for using such tech stacks <br>
**1. Why choose Golang as backend language?**<br>
* Go is built for system application, web application, i.e., we don’t need Django for python and TomCat for java. It has built-in web services to handle HTTP request/response, URL mapping…
* Go has been very well designed and optimised for scaling(Go routine and channel)
* Go is a fast, statically typed, compiled language that feels like a dynamically typed, interpreted language.
* In my opinion, go is the future of server language.

**2. Why choose ElasticSearch?**<br>
* ES is an open source, distributed, RESTful search engine. As the heart of the Elastic Stack, it centrally stores our data so we can query the data quickly.
* ES is implemented by K-D tree, which is very efficient to perform 2-dimensional search problem(e.g. given latitute and longitute, search all posts in that position within 200km)
* I also use ES to store structured data(post's content, lat and lon)

**3. Why choose Google App Engine Flex?**<br>
* Google App Engine is a cloud computing platform for developing and hosting web applications in Google-managed data centers.
* Actually, GAE is based on GCE, however, by using GAE, I don't need to build a system by myself, load balancer and VMs are implemented in black box in GAE.
* App Engine flexible environment automatically scales app up and down while balancing the load. Microservices, authorization, SQL and NoSQL databases, traffic splitting, logging. 

**4. Why choose Google Compute Engine?**<br>
* GCE is similar to Amazon EC2, which offers virtual machines (Xen, KVM, VirtualBox, VMware ESX, etc.). Amazon EC2 and Google Compute Engine belong to IaaS as well. 
* I install ES in the VM(Ubuntu 16) in the GCE to store post information and perform search.

**5. Why use Google Cloud Storage(GCS)?**<br>
* To store unstructured data (user posted images). BigTable, BigQuery, ElasticSearch are all for structured data. GCS is well-known for its durability, scalability and availability.

**6. Why use BigTable instead of MySQL or MangoDB?**<br>
* I want to make this project scalable, MySQL do not have enough scability. MongoDB is good, but it is just software, in order to make it scalable, I need to rent vms and clusters, to build system by myself. BigTable is a sparse, distributed, persistent multidimensional sorted map. It runs on Google cloud, which is very scalable, I only need to implement my business logic, which is very convenient. 

**7. Why use BigTable since I already use ES to save post data?**<br>
* I want to do offline data analysis on it using BigQuery. ES is search engine with complicated query support and better read performance but may lose data, so I save posts data on ES and Bigtable both.
* Save post data to ES is for geo-index based range search. Save post data to BigTable is for offline data analysis using BigQuery latter.

**8. Why use DataFlow and BigQuery?**<br>
* I want to do some data analysis of user posted data(like sentiment analysis, spam filter..). BigTable is designed for saving large volume of data but not good for querying (JOIN, ORDER BY etc.). BigQuery, instead, is efficient for querying. It is Google's fully managed, petabyte scale, low cost enterprise data warehouse. It supports data analysis by using SQL syntax.
* In order to dump data into BigQuery from BigTable, I need to use Google DataFlow, which runs in Google's cloud platform, who can help me to transform my data from BigTable to BigQuery easily.

**9. Why use Cache?**<br>
* When performance needs to be improved, caching is often the first step to take, so I decide to place previously requested information (search query) in cache to speed up data access and reduce bandwidth demand. In this project, I use TTL(time to live) as caching Strategy.

**10. Why Choose Redis instead of Memcached?**<br>
* Redis is an open source, in-memory data structure store, used as a database, cache and message broker. Basically a key-values store. Google Cloud has a memcache similarly but it does not support GAE flex yet.
* Redis has more features than Memcached and is, thus, more powerful and flexible. Refered this link: https://www.infoworld.com/article/3063161/nosql/why-redis-beats-memcached-for-caching.html

## Tools
GoLand
Google Cloud Platform
Apache Maven(it will install all dependencies(dataflow related libraries) automatically, and can create Java project and manage it for you)

## Requirements

## Installation

## Usage/Quick Start

## Screenshots

## Known issues

## Todo list
* Front-end implementation using React.js.

## Version Change Log
1. v0.0.1(05/23/2018)<br>
* Create GCE(Google Compute Engine) instance and install elastic search(golang version).
* Designed and create handlerSearch and handlerPost functions' API.
2. v0.0.2(05/24/2018)<br>
* Finish the implemententation of handlerSearch, handlerPost, tested on the local machine and GCE.
3. v0.0.3(05/30/2018)<br>
* Add filter sensitive words function in search method(Spam and Abuse Detection).
* Deploy backend to Google App Engine.
4. v0.0.4(06/07/2018)<br>
* Create bucket in Google Cloud Storage(GCS).
* Update handlerPost function to parse multipart form and store post with image file into GCS.
5. v0.0.5(06/11/2018)<br>
* Update handlerPost function to support save post data into BigTable.
6. v0.0.6(06/23/2018)<br>
* Applied Google dataflow to dump post data from BigTable to BigQuery.
7. v0.0.7(07/01/2018)<br>
* Integreted OAuth 2.0 to support authentication and user signup.
8. v0.0.8(07/17/2018)<br>
* Used Redis to cache search requests to speed up user query.
9. v0.0.9(08/23/2018)<br>
* Init front-end React project.

## Licenses
NAN

## Notes
If you have any questions, feel free to contact me at jiaxig@ece.ubc.ca

