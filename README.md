# How to install and launch ChatSystem project
## Environement
We have used Java 13.0.2 and SQLite JDBC 3.30.1 for our **Eclipse** project. The Eclipse version is 4.12.0 (release from june 2019). 

## Installation
Just clone or download the project from GitHub with this link :
*https://github.com/Marie1009/COO_POO_ChatSystem.git*
The master branch contain the UML diagrams for the conception part and the project branch contain the Eclipse project.

The command line is then : 
**git clone -b projet https://github.com/Marie1009/COO_POO_ChatSystem.git**

The SQL Java library is already contained in the lib directory of the project.
To consult the JavaDoc provided with our project just open the index.html file in the javadoc directory in a navigator. 

## Launching

It is important to use a different computer for each launch of the ChatSystem. Indeed, some port numbers are common to all instances and there will be binding errors with several launches in the same system. The other point is that these different computer must have different files systems (for example connecting in ssh to another computer with the same INSA login would not be good) otherwise all the running applications would be updating the same database.  
The db directory containing the local database must be empty for the first launch. Just check the db directory exists, in the root of the project, and that it is empty before launching.

To launch the project you can either launch via Eclipse with the Run button (or right click on the Launcher class and select *Run As > Java Application* or launch the launcher.jar at the project root. To do so open a terminal in the COO_POO_ChatSystem directory and execute **./launcher.jar**. The .jar file has been compiled under a Ubuntu environement, version 18.04. 


