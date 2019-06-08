# CS166-SQL-final-project

*Short description of your project, in one or two sentences.* 

## Languages / Frameworks used

* PostgreSQL (PSQL)
* Java
* Bash

## Purpose / Goal

* To create an Airport database management system using PSQL, executable using bash, and interactable with Java
* Complete with schema for customers, flightinfo, flights, pilots, planes, repairs, reservations, schedules, and technicians.

### Assumptions

* Plane make and model do not necessarily have to be strictly alphabetical
* Plane age cannot be zero or greater than 30, or else the plane will be retired
* Number of seats on the plane cannot be zero, negative, or greater than 853 (highest capacity for commercial aircraft right now)
* Pilot fullname does not necessarily have to be strictly alphabetical
* Pilot nationality does not necessarily have to be strictly alphabetical
* Technician fullname does not necessarily have to be strictly alphabetical
* Airport code should be 5 long.
* Departure date should be come before arrival date.
* Library java.time.LocalDate, java.lang.Object, java.time.format.DateTimeFormatter added to manipulate date type data.
* Reservation status should be capital letter. E.g. C, W, R. Not c, w, r.
* For BookFlight, you can edit status if flight number and customer ID are matched.
* If flight number and customer ID are not matched, you are making a new reservation with new reservation number and customer status.
* For ListNumberOfAvailableSeats, date is not considered as variable. It will list only with flight number and (Plane seat - number of sold seats).


### Installation

* Mac OS X
* Ubuntu
* Windows (if applicable)
* PostgreSQL
* Text Editor (VS Code, emacs, etc.)

### Getting started

* Clone the repository
* Navigate to the "postgresql" folder in your terminal
* Run the “startPostgreSQL.sh” script using the command “source ./startPostgreSQL.sh”
* Run the “createPostgreDB.sh” script using the command “source ./createPostgreSQL.sh”
* Navigate to the “java” folder
* Run the “compile.sh” script using the command “./compile.sh”
* Run the “run.sh” script using the command “./run.sh $USER_DB $PGPORT $USER”
* Input menu number and check the result
* Input 10 to exit

## Usage / Complete Functionalities

* **Add plane**: Ask the user for details of a plane and add it to the DB
* **Add pilot**: Ask the user for details of a pilot and add it to the DB 
* **Add Flight**: Ask the user for details of a flight and add it to the DB 
* **Add Technician**: Ask user for details of a technician and add it to the DB
* **Book flight**: Given a customer and flight that he/she wants to book, determine the status of the reservation (Waitlisted/Confirmed/Reserved) and add the reservation to the database with appropriate status.
* **List number of available seats for a given flight**: Given a flight number and a departure date, find the number of available seats in the flight.
* **List total number of repairs per plane in descending order**: Return the list of planes in decreasing order of number of repairs that have been made on the planes.
* **List total number of repairs per year in ascending order**: Return the years with the number of repairs made in those years in ascending order of number of repairs per year
* **Find total number of passengers with a given status**: For a given flight and passenger status, return the number of passengers with the given status.
* **Physical DB Design** (DB performance tuning indexes)

## DB Performance

* Each attribute is indexed using B+ tree.
* Performance is measured with “\timing” using DB sql.
* Each measured indexed performance is recorded in DBproject.java, and also not indexed performance is recorded to compare both different design.
* Each recorded time is averaged with 10 times execution.

## Demo

**Booking a flight, and customer ID is found in the database**

![image](https://user-images.githubusercontent.com/14877762/59150571-86dcbd80-89da-11e9-827d-caadd1e50b49.png)

**Total number of seats available for a given flight**

![image](https://user-images.githubusercontent.com/14877762/59150583-affd4e00-89da-11e9-8093-8ac9c9e82820.png)

## Team

* Adrian Tran
* Minwhan Oh

## Errors and bugs

If something is not behaving intuitively, it is a bug and should be reported.
Report it here by creating an issue: https://github.com/adrianmoo2/CS166-SQL-final-project/issues

Help us fix the problem as quickly as possible by following [Mozilla's guidelines for reporting bugs.](https://developer.mozilla.org/en-US/docs/Mozilla/QA/Bug_writing_guidelines#General_Outline_of_a_Bug_Report)

## Patches and pull requests

Your patches are welcome. Here's our suggested workflow:
 
* Fork the project.
* Make your feature addition or bug fix.
* Send us a pull request with a description of your work. Bonus points for topic branches!

## Copyright and attribution

Copyright (c) 2019 Adrian Tran. Released under the [MIT License](https://github.com/adrianmoo2/CS166-SQL-final-project/blob/master/LICENSE).
