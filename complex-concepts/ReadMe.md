Complex Microservices Concepts
==============================
This code demonstrates developing advanced/complex microservices including 
far jars, health, dynamic discovery, containerized deployments as well as cloud 
deployments using Java EE, WildFly Swarm, Docker and AWS.

Part of this code is used as a demo for 
[this] (http://www.slideshare.net/reza_rahman/javaee-microservices) talk. A
video for the talk can be found [here](https://www.youtube.com/watch?v=bS6zKgMb8So).
The code is derived from the [Cargo Tracker](https://cargotracker.java.net/)
Java EE blue prints project. 

The overall application is broken up into two logical units - Cargo Tracker and 
Path Finder. The Cargo Tracker application is a larger Java EE war deployed to
GlassFish. In microservices parlance the Cargo Tracker application is a so-called 
monolith. The Path Finder microservice is developed as a Java EE fat jar using 
WildFly Swarm. Cargo Tracker uses the smaller Path Finder microservice.

This code uses GlassFish 4.1. It should be possible to use any Java EE 7 
compatible application server such as Payara, WildFly, JBoss EAP, 
WebSphere Liberty or WebLogic. We use WildFly Swarm as our fat-jar solution. It
should be possible to use any other MicroProfile compatible runtime such as 
Payara Micro or TomEE embedded. We use NetBeans but you can use any Maven 
capable IDE.

Setup
-----
* Download this directory somewhere into your file system.
* Make sure you have JDK 8+ installed.
* Please install NetBeans 8+. Make sure to download the Java EE edition.
* Download GlassFish 4.1 from [here]
(https://glassfish.java.net/download-archive.html). Make sure to download the 
full platform, not the web profile. Please do not use GlassFish 4.1.1 - it 
has bugs that will stop the application from working properly.
* Please unzip the zip file anywhere in your file system.
* NetBeans bundles an outdated version of Maven by default. WildFly Swarm 
needs an updated version of Maven. Please download and install the latest
version of [Maven](https://maven.apache.org/download.cgi).
* Start NetBeans. Point NetBeans to the updated Maven installation by going to
NetBeans -> Preferences -> Java -> Maven -> Maven Home. 
* You now need to setup GlassFish in NetBeans. You do that by going to 
Services -> Servers -> Add Server -> GlassFish Server. Enter the location of 
the GlassFish directory. Choose the defaults in the next few screens to register 
GlassFish with NetBeans.

Instructions
------------
* There are two separate Maven projects in the directory - cargo-tracker and 
path-finder. You need to open and build both projects in NetBeans.
* You first need to run the path-finder microservice. You can run it through 
Project -> Run Maven -> Run with WildFly Swarm. This is a custom NetBeans action
that executes the wildfly-swarm:run Maven goal. Please wait for the project to 
start. You can also run the path-finder application by going to it's target 
directory and executing:
```
java -jar path-finder-1.0-swarm.jar
```
* Next you will run the cargo-tracker application. You will need to specify that 
the cargo-tracker project will run with GlassFish. You do that by going to 
Project -> Properties -> Run -> Server and choosing GlassFish. You will run the 
Cargo Tracker application by selecting Project -> Run. When the application 
finishes deploying, NetBeans should automatically open a browser window with the 
running application.
* You need to book and route a cargo. Please take a look at the video for the 
Cargo Tracker application on how to do this or look through the readme of the 
original Cargo Tracker application. The Path Finder service is used for
routing by Cargo Tracker.
* The Path Finder microservice is equipped with some basic health check 
facilities using the WildFly Swarm monitor fragment. You can check them out by
navigating to http://localhost:8888/node, http://localhost:8888/heap and 
http://localhost:8888/threads.