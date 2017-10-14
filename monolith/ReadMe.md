Cargo Tracker Monolith
======================
This is a vanilla Java EE application of moderate complexity. In microservices 
parlance, the application can be considered a monolith.

Part of this code is used as a demo for 
[this](http://www.slideshare.net/reza_rahman/javaee-microservices) talk. A
video for the talk can be found [here](https://www.youtube.com/watch?v=bS6zKgMb8So).
The code is derived from the [Cargo Tracker](https://cargotracker.java.net/)
Java EE blue prints project.

The application uses GlassFish 4.1. It should be possible to use any Java EE 7 
compatible application server such as Payara, WildFly, JBoss EAP, 
WebSphere Liberty or WebLogic. We use NetBeans here but you can use any Maven 
capable IDE.

Setup
-----
* Download the code in this directory somewhere into your file system.
* Make sure you have JDK 8+ installed.
* Please install NetBeans 8+. Make sure to download the Java EE edition.
* Download GlassFish 4.1 from [here](https://glassfish.java.net/download-archive.html). Make sure to download the 
full platform, not the web profile. Please do not use GlassFish 4.1.1 - it 
has bugs that will stop the application from working properly.
* Please unzip the zip file anywhere in your file system.
* You need to setup GlassFish in NetBeans. You do that by going to 
Services -> Servers -> Add Server -> GlassFish Server. Enter the location of 
the GlassFish directory. Choose the defaults in the next few screens to register 
GlassFish with NetBeans.

Instructions
------------
* Start NetBeans. There is a Maven project in the directory - cargo-tracker. You 
need to open and build the project in NetBeans.
* You will need to specify that the project will run with GlassFish. You do that 
by going to Project -> Properties -> Run -> Server and choosing GlassFish.
* When ready, you need to run the cargo-tracker application (Project -> Run). 
Wait for the project to deploy and run. NetBeans should automatically open a 
browser window with the running application.
* You need to book and route a cargo. Please take a look at the video for the 
Cargo Tracker application on how to do this or look through the readme of the 
Cargo Tracker application.
