Boys LAX Relative Power Index
===================================

Deployment
==========

Is as easy as:

    mvn clean appengine:update -Pupdate
    
    
Starting
========

Use the devserver of GAE:

    mvn appengine:devserver -Pdevserver

Issue a request to http://localhost:8080/recache to populate the database

Issue a request to http://localhost:8080/recalculate to calculate RPI

To clear the database in dev mode
 
 	mvn clean