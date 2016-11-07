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
* We use Consul for service registration.
[Download and install Consul] (https://www.consul.io/intro/getting-started/install.html) 
for your OS.

Instructions
------------
* You must start Consul:
```
consul agent -dev
```
* Please verify that Consul is running by going to <http://localhost:8500/>. 
There won't be much to see right now. As the microservice comes up, you'll see
it show up here. 
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
navigating to <http://localhost:8888/node>, <http://localhost:8888/heap>, 
<http://localhost:8888/threads> and <http://localhost:8888/health>.

Docker Instructions
-------------------
We will run all our code with Docker. Docker is common in DevOps, microservices 
and cloud deployments as a lightweight alternative to traditional 
virtualization.

* [Install Docker](https://docs.docker.com/engine/installation/) on your OS.
* Please make sure Docker is running.
* Run Consul using Docker: 
```
docker run -it --rm -p 8500:8500 consul
```
* Verify that Consul is running by going to <http://localhost:8500/>.
* Because Docker containers are run in virtual networks isolated from the 
host machine, we will no longer be able to use 'localhost' or 127.0.0.1 
reliably. We will need to change over IP address references because of this 
fact. Fortunately in the current version of Docker, we can use 172.17.0.1 as a 
rough equivalent to 127.0.0.1 to refer to the host machine.
* We will need to make a couple of changes to the 
[POM file for Path Finder](path-finder/pom.xml). The swarm.bind.address property
will need to refer to the IP of the container that Path Finder will be running 
on. On Mac OS, it is convenient to simply change this to 0.0.0.0. We should 
also change swarm.consul.url to 172.17.0.1 from localhost.
* The [Cargo Tracker EJB-JAR XML](cargo-tracker/src/main/webapp/WEB-INF/ejb-jar.xml) 
will need similar changes. Change over the pathFinderDiscoveryUrl value to
172.17.0.1 from localhost. The [External Routing Service](cargo-tracker/src/main/java/net/java/cargotracker/infrastructure/routing/ExternalRoutingService.java)
'address' variable should also be hard-coded to 172.17.0.1.
* In NetBeans, both Cargo Tracker and Path Finder should now be rebuilt 
(Project -> Clean and Build).
* Please copy over path-finder-1.0-swarm.jar from the Maven Target directory to
the project's [path-finder/src/docker/](path-finder/src/docker/) directory.
* Open up the terminal and change directories to [path-finder/src/docker/](path-finder/src/docker/).
* We will now build a Docker image for Path Finder using:
```
docker build -t path-finder .
```
* You will now run Path Finder through Docker:
```
docker run -it --rm -p 8888:8888 path-finder
```
* Make sure Path Finder is running by navigating to <http://localhost:8888/health>.
* Please copy over cargo-tracker.war from the Maven Target directory to
the project's [cargo-tracker/src/docker/](cargo-tracker/src/docker/) directory.
* Open up the terminal and change directories to [cargo-tracker/src/docker/](cargo-tracker/src/docker/).
* We will now build a Docker image for Cargo Tracker using:
```
docker build -t cargo-tracker .
```
* You will now run Cargo Tracker through Docker:
```
docker run -it --rm -p 8080:8080 cargo-tracker
```
* Make sure Cargo Tracker is running by navigating to <http://localhost:8080/cargo-tracker>.