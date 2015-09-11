
# Integration of Heterogeneous Data Sources for Financial Risk Calculations Using Cache Plugins

## Birkbeck, University of London  
## Department of Computer Science and Information Systems  

## MSc Computing for the Financial Services  
## Project Report  
## 2015



Supervisor:   Dr Nigel Martin

Author:	      David Lord

## Dependencies
To build this project you will need to the following :

* [Java SE Development Kit 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or above

* [Maven 3.3.3](https://maven.apache.org/download.cgi) or above

## Installation
Then you can download this repository with

	git clone https://github.com/BBK-SDP-2015-dlord03/project.git

## Building
Then simply navigate into the newly cloned example folder and run:

    mvn package

This will compile, test and package all the modules in the project. When the build is complete you should see the following message:

	[INFO] Cache Plugin Project ............................... SUCCESS [  0.114 s]
	[INFO] Plugin API ......................................... SUCCESS [  2.328 s]
	[INFO] Plugin Providers ................................... SUCCESS [  0.002 s]
	[INFO] Dividend Plugin .................................... SUCCESS [  0.293 s]
	[INFO] Volatility Plugin .................................. SUCCESS [  0.132 s]
	[INFO] Option Plugin ...................................... SUCCESS [  0.180 s]
	[INFO] Cache Query Service ................................ SUCCESS [  2.335 s]
	[INFO] Web Service ........................................ SUCCESS [  0.625 s]
	[INFO] Distribution ....................................... SUCCESS [  4.943 s]


## Running the Sample

After building the project as shown above you should now have a new folder called `distribution/target/distribution-1.0-SNAPSHOT/` within the project directory. To start the demonstration web services application `cd` to that directory and type in the following:

	%JAVA_HOME%\bin\java -classpath * uk.ac.bbk.dlord03.webservice.Main

The sample application should then start and be available at the URL `http://localhost:8080`. Some example queries are shown below.

| URL | Description |
|-----|-------------|
| http://localhost:8080/option/VOD151016C00044000 | Get the latest version of the option with [OCC Code](https://en.wikipedia.org/wiki/Option_symbol) VOD151016C00044000 |