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
[this](http://www.slideshare.net/reza_rahman/javaee-microservices) talk. A
video for the talk can be found [here](https://www.youtube.com/watch?v=bS6zKgMb8So).
The code is derived from the [Cargo Tracker](https://cargotracker.java.net/)
Java EE blue prints project. 

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
* There are three distinct sections of the lab separated into separate directories
of the zip - [monolith](monolith/), [javaee-microservices](javaee-microservices/) 
and [swarm-microservices](swarm-microservices/). We will explore each in turn.
* After discussing the basic concepts behind microservices, we will explore the
code under [monolith](monolith/). The directory contains a single vanilla 
Java EE application named Cargo Tracker. In microservices parlance, the 
application can be considered a monolith. Please follow the instructions in the 
directory to get the application running.
* Having explored the Cargo Tracker Java EE application, we will discuss how the 
routing service can be separated into a separate REST service. We will call this
separate service the path-finder application. In microservices parlance, Path 
Finder can be considered a microservice. We will develop path-finder as a 
separate Maven war project using JAX-RS, EJB, CDI and Bean Validation. The 
Cargo Tracker application will use the JAX-RS client API to access the 
Path Finder application. Cargo Tracker and Path Finder can be deployed to the 
same GlassFish instance or different GlassFish instances.
* The [javaee-microservices](javaee-microservices/) directory has a possible 
solution for breaking up the Cargo Tracker application into simple, practical 
vanilla Java EE based microservices. You should follow the instructions in the 
directory to get the solution running and explore it. We will discuss this 
solution in detail.
* Having discussed how we can develop simple microservices using vanilla Java EE
war files, we are ready to introduce WildFly Swarm as a fat jar solution. 
Introducing WildFly Swarm also enables us to add other features besides fat jars
such as dynamic discovery, health-check, circuit-breakers/bulkheads and 
client-side load-balancing. After discussing these concepts, we will convert 
path-finder from a war file to a self-contained fat-jar.
* The [swarm-microservices](swarm-microservices/) directory has a possible 
solution for converting path-finder into a WildFly Swarm based fat jar.
You should follow the instructions in the directory to get the solution running 
and explore it. We will discuss this solution in detail.
* The [complex-concepts](complex-concepts/) directory contains some examples of 
more advanced concepts such as health-check, dynamic discovery, containers 
and cloud deployment. We will discuss these concepts as time permits.

To Do
-----
* Use AWS or Jelastic.
* Move from GlassFish to WildFly.
