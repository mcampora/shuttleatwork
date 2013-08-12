Objective
---------
GTFS prototype.

The objective is to build an easy to use Web application one can use to find the next shuttle departure.

The application is designed to be deployed on [Google App Engine](http://shuttleatwork.appspot.com/) and run in a mobile browser (ie. tablet or smartphone).

![Screenshot](/screen.png)

Technical environment
---------------------
The server side application is developed in Groovy
The client side relies on JQuery mobile and the Google Map API

Come as an Eclipse project.
Probably work with different versions however I'm using Eclipse Juno, Groovy and Google App Engine features on Mac OS. 

GTFS files are stored under [project]/src/rsc. Current files capture my company shuttle, routes and schedules connecting the various buildings.

Check the associated Wiki for more details.