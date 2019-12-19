
Project name: 
Techical Indicator Project:

Description: 
This project is created specifically for technical traders to:
1.	Create and combine technical indicators
2.	Alerts when certain technical indicator matures
3.	Create and Manage Notes of interesting stocks
4.	Create watchlist of stocks
5.	Search market news

Architecture:
This newest version consist of 3 main modules
1.	[Repo] baseJ : this is build on SpingBoot JPA , QueryDSL , Postgress
2.	[Elastic]Elastic: this store all the market news in ElasticSearch
3.	[In Memory Repo] base : this is image of repo , but is in memory using IGNITE
4.	[Data Repo] base: this caches all the user customs technical indicators.
5.	[Logic] taj4j: This is most of the business logic.
6.	[Controller] wishlist , Oauth , User :  
7.	[Frontend] AngularJS

Infrastructure:
These apps are hosted on Rancher Kubernetes , running on single worker nodes. All the apps are single instances except for the logic (taj4j) app which runs on 2 instances to improve performance.
All apps uses Spring Boot Actuator , to generate heartbeats to Rancher, if in any instance that an apps dies , it will be automatically be restarted by Rancher.
All codes are build and deploy by Jenkins , subsequently Helm chart is used to deploy apps into Rancher.

License:
1.	Java 8
2.	Kotlin
3.	Ignite
4.	Spring Boot
5.	QueryDSL
6.	OAuth2
7.	Ta4j
8.	Postgress
9.	ElasticSearch
10.	Rancher
11.	Kubernetes
12.	Helm
13.	Jenkins
14.	Docker Repo
15.	AngularJS

Future Development:
1.	HTTPS Certificates
2.	User Feedback pages
3.	More detail management of Comments
4.	Adding more technical Indicators
