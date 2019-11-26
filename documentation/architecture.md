**Application Logic**

There are 2 classes which form most of the functionality of the application, Row and Fight, shown below.  
![architecture](/documentation/images/architecture.png)

In addition to these two, classes Reader and Stats contain static methods for the diffrent services provided by the application

Reader provides the static method  
  * readFile(String path) : ArrayList<Fight>  
 
Which is responsible for reading a given log file (Reader also contain static methods for determining the name of a file from it's absolute path, and a date from the name of a log. As logs have default names which contain the date)

The class Stats contains numerous static methods for calculating different metrics about a specfic fight, such as
  * getAllDamageByOwner(Fight fight) : int
  * dps(Fight fight) double
  * getDuration(Fight fight) long
  * divideDamageDealtByAbility (Fight fight) : HashMap<String, Integer>
  * etc.  
  

Seperate from these is the Database class which can store the information in a Row object into an sql database, and create Row objects based on the information sotred inside a database.
