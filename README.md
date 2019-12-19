
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
2.	[In Memory Repo] base : this is image of repo , but is in memory using IGNITE
3.	[Data Repo] base: this caches all the user customs technical indicators.
4.	[Logic] taj4j: This is most of the business logic.
5.	[Controller] wishlist , Oauth , User :  
6.	[Frontend] AngularJS

Infrastructure:
These apps are hosted on Rancher Kubernetes , running on single worker nodes. All the apps are single instances except for the logic (taj4j) app which is run on 2 instances to improve performance.
All apps uses Spring Boot Actuator , to generate heartbeats to Rancher, if in any instance that an apps dies , it will be automatically be restarted by Rancher.
All codes are build and deploy by Jenkins , subsequently Helm chart is used to deploy apps into Rancher.

License:
1.	Java 8
2.	Kotlin
3.	Ignite
4.	Spring Boot
5.	QueryDSL
6.	Ta4j
7.	Postgress
8.	Rancher
9.	Kubernetes
10.	Helm
11.	Jenkins
12.	Docker Repo
13.	AngularJS

