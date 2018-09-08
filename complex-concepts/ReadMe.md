Complex Microservices Concepts
==============================
This code demonstrates developing advanced/complex microservices including 
far jars, health, dynamic discovery, containerized deployments as well as cloud 
deployments using Java EE, MicroProfile, Docker and AWS.

Part of this code is used as a demo for 
[this](https://speakerdeck.com/reza_rahman/down-to-earth-microservices-with-java-ee) talk. A
video for the talk can be found [here](https://www.youtube.com/watch?v=bS6zKgMb8So).
The code is derived from the [Cargo Tracker](https://cargotracker.java.net/)
Java EE blue prints project. 

The overall application is broken up into two logical units - Cargo Tracker and 
Path Finder. The Cargo Tracker application is a larger Java EE war deployed to
GlassFish. In microservices parlance the Cargo Tracker application is a so-called 
monolith. The Path Finder microservice is developed as a MicroProfile fat jar.
Cargo Tracker uses the smaller Path Finder microservice.

This code uses Payara Server 5. It should be possible to use any Java EE 8
compatible application server such as GlassFish, WildFly or WebSphere Liberty.

We use Payara Micro 5.183 as our fat-jar solution. It should be possible to use any 
other MicroProfile 2.0 compatible runtime such as WildFly Swarm, Thorntail or TomEE.
We use NetBeans but you can use any Maven capable IDE.

Setup
-----
* Download this directory somewhere into your file system.
* Make sure you have JDK 8+ installed.
* Please install NetBeans 8+. Make sure to download the Java EE edition.
* Download Payara Server 5 from [here](https://www.payara.fish). Make sure to download the 
full platform, not the web profile.
* Please unzip the zip file anywhere in your file system.
* Start NetBeans. Point NetBeans to the updated Maven installation by going to
NetBeans -> Preferences -> Java -> Maven -> Maven Home. 
* You now need to setup Payara Server in NetBeans. You do that by going to 
Services -> Servers -> Add Server -> GlassFish Server. Enter the location of 
the Payara Server directory. Choose the defaults in the next few screens to register 
Payara Server as a GlassFish Server with NetBeans.
* build the project with `mvn install` - this will build all the subprojects

Instructions for running with discovery over Payara cluster
------------

* You first need to run the path-finder microservice. You can run it through 
Project -> Run Maven -> Run with Payara Micro. This is a custom NetBeans action
that executes the payara-micro:start Maven goal. Please wait for the project to 
start. You can also run the path-finder application by going to it's target 
directory and executing:
```
java -jar path-finder-1.0-microbundle.jar
```
* Next you will run the cargo-tracker application. You will need to specify that 
the cargo-tracker project will run with Payara Server. You do that by going to 
Project -> Properties -> Run -> Server and choosing Payara Server. You will run the 
Cargo Tracker application by selecting Project -> Run. When the application 
finishes deploying, NetBeans should automatically open a browser window with the 
running application.
* You need to book and route a cargo. Please take a look at the video for the 
Cargo Tracker application on how to do this or look through the readme of the 
original Cargo Tracker application. The Path Finder service is used for
routing by Cargo Tracker.
* TODO: rewrite for Microprofile -> The Path Finder microservice is equipped with some basic health check 
facilities using the WildFly Swarm monitor fragment. You can check them out by
navigating to <http://localhost:8888/node>, <http://localhost:8888/heap>, 
<http://localhost:8888/threads> and <http://localhost:8888/health>.


Instructions for running with discovery using Consul
------------

* You must start Consul:
```
consul agent -dev
```
* Please verify that Consul is running by going to <http://localhost:8500/>. 
There won't be much to see right now. As the microservice comes up, you'll see
it show up here. 
* Modify pom.xml of the path-finder microservice to use the `consul-discovery` dependency instead of the `jcache-discovery` dependency
* Continue with the instructions for running with discovery over Payara cluster

Docker Instructions
-------------------
We can also run all our code with Docker. Docker is common in DevOps, 
microservices and cloud deployments as a lightweight alternative to traditional 
virtualization.

* [Install Docker](https://docs.docker.com/engine/installation/) on your OS.
* Please make sure Docker is running.
* Run Consul using Docker: 
```
docker run -it --rm -p 8500:8500 consul
```
* Verify that Consul is running by going to <http://localhost:8500/>.
* Docker containers are run in a virtual network isolated from the host machine
using NAT. The Docker engine/host serves as the NAT gateway and is assigned the IP
172.17.0.1. Each additional container started is assigned an IP in the sequence
172.17.0.2, 172.17.0.3, 172.17.0.4 and so on. As a result, we can actually 
simulate the situation where Consul, Path Finder and Cargo Tracker are running 
on separate machines. If we start containers in the order suggested here, 
Consul will have the IP 172.17.0.2, Path Finder will have the IP 172.17.0.3 and
Cargo Tracker will have the IP 172.17.0.4. We will change over our IP address 
references accordingly because of this fact.
* We will need to make a couple of changes to the 
[POM file for Path Finder](path-finder/pom.xml). The swarm.bind.address property
will need to refer to the IP of the container that Path Finder will be running 
on - 172.17.0.3. We also need to change swarm.consul.url to 172.17.0.2 from 
localhost.
* The [Cargo Tracker EJB-JAR XML](cargo-tracker/src/main/webapp/WEB-INF/ejb-jar.xml) 
will need similar changes. Change over the pathFinderDiscoveryUrl value to
172.17.0.2 from localhost.
* In NetBeans, both Cargo Tracker and Path Finder should now be rebuilt 
(Project -> Clean and Build) before proceeding further.
* Please copy over path-finder-1.0-swarm.jar from the Maven target directory to
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
* Please copy over cargo-tracker.war from the Maven target directory to
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
* Make sure Cargo Tracker is running by navigating to 
<http://localhost:8080/cargo-tracker> and routing a cargo.
