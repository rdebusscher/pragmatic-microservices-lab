Pragmatic Microservices with Java EE and WildFly Swarm
======================================================
This hands-on lab demonstrates developing reasonable microservices appropriate 
for most ordinary blue collar IT organizations in a step-by-step fashion. We 
initially focus on nothing but vanilla Java EE and simple, fast deploying thin 
war files. We then also cover concepts such as fat-jars, dynamic discovery, 
circuit-breakers and client-side load-balancing using WildFly Swarm. Lastly we 
will also discuss container solutions like Docker and cloud platforms such as
AWS.   

Part of this code is used as a demo for 
[this] (http://www.slideshare.net/reza_rahman/javaee-microservices) talk. A
video for the talk can be found [here](https://www.youtube.com/watch?v=bS6zKgMb8So).
The code is derived from the [Cargo Tracker](https://cargotracker.java.net/)
Java EE blue prints project. 




Although logically two separate applications that 
project is deliberately structured as a single war for simplicity. This project
breaks the code up into two separate wars. The larger war represents the Cargo Tracker
application. In microservices parlance the Cargo Tracker application is a so
called monolith. The smaller war file represents the Path Finder microservice. Cargo
Tracker uses the smaller Path Finder service.

This lab uses GlassFish 4.1. It should be possible to use any Java EE 7 
compatible application server such as Payara, WildFly, JBoss EAP, 
WebSphere Liberty or WebLogic. We use WildFly Swarm as our fat-jar solution. It
should be possible to use any other MicroProfile compatible runtime such as 
Payara Micro or TomEE embedded. We use NetBeans but you can use any Maven 
capable IDE. 

Setup
-----
* Download this project somewhere into your file system, probably as a zip file 
(and extract it).
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
There are three distinct sections of the lab separated into separate directories
of the zip - (monolith), (javaee-microservices) and (swarm-microservices)

* There are two separate Maven projects in the zip you downloaded - cargo-tracker
and path-finder. They are both in their own  directories under the root directory. You need
to open and build both projects in NetBeans.
* You will need to specify that both projects will run with GlassFish. You do that by going to 
Project -> Properties -> Run -> Server and choosing GlassFish.
* When ready, you will first run the path-finder application (Project -> Run). Wait for the project to
deploy and run. Then you will similarly run the Cargo Tracker application. In both cases, NetBeans
should automatically open a browser window with the running application.
* You need to book and route a cargo. Please take a look at the video for the talk on how to do this or
look through the readme of the original Cargo Tracker application. The Path Finder service is used for
routing by Cargo Tracker.
* In this demo both Cargo Tracker and Path Finder run on the same GlassFish domain. If you want you can 
run the two wars on two different servers or two different GlassFish domains. 
Just make the appropriate changes in [ejb-jar.xml for Cargo Tracker]
(cargo-tracker/src/main/webapp/WEB-INF/ejb-jar.xml) to point it to the location of the Path Finder
service. Most servers will also allow you to change the JNDI entry value at runtime through
GUI administrative tools without any changes to the war. You can also use load balancers and DNS
with the two applications if you like to add greater flexibility or fault tolerance.