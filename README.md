### BIRKBECK, UNIVERSITY OF LONDON
#### DEPARTMENT OF COMPUTER SCIENCE AND INFORMATION SYSTEMS

#### MSC COMPUTING FOR THE FINANCIAL SERVICES
#### PROJECT REPORT
#### 2015

# INTEGRATION OF HETEROGENEOUS DATA SOURCES FOR FINANCIAL RISK CALCULATIONS USING CACHE PLUGINS

Supervisor:   Dr Nigel Martin

Author:	      David Lord

# Task List
* TODO Add cache invalidation in query service.
* TODO Add cache invalidation into unit tests and provide example in report.
* TODO Add JavaDoc coverage policy into this README.
* TODO Add JavaDoc to all classes 
* TODO Implement plugin providers.
* TODO Simplify cache interface by removing Jersey?
* TODO Add instructions for building and running reference implementation to this README.
* TODO Add FindBugs report into Maven build and include screen shot in report.
* TODO Add CheckStyle report into Maven build and include screen shot in report.

# matic-very-simple-example

A very simple example project for Matic using one Jade template and one schema file.

## Deprecated
Please see [Matic-draft4-example](https://github.com/mattyod/matic-draft4-example).

## Dependencies
To build this example you will need to install the following two node packages:

[Matic](https://github.com/mattyod/matic)

and

[Jade](https://github.com/visionmedia/jade)

## Installation
Then you can download this repository with

    git clone https://github.com/mattyod/matic-very-simple-example.git

## Building the example docs
Then simply navigate into the newly cloned example folder and run:

    matic

You should now have a new folder called 'web' within the sample project. Within this should be a file called very-simple-example.html, open this with a browser and view the generated documentation.

Please note this particular example is built to work as a simple local flat file system and so paths are not set as they would be if files were hosted on a server. That means you can just double click the generated HTML file and view it in your default browser.

[An example of the output](http://mattyod.github.com/matic-very-simple-example/).