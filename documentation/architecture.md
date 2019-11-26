**Application Logic**

There are 2 classes which form most of the functionality of the application, Row and Fight, shown below.  
[architecture](documentation/images/architecture.png)

In addition Reader provides the static method  
  * readFile(String path) ; ArrayList<Fight>  
Which is responsible for reading a given log file (Reader also contain a few methods for determining the name of a file from it's absolute path, and a date from the name of a log, logs have default names which contain the date)

The class Stats contains numerous static methods for calcluating different metrics about a specfic fight, such as
  * getAllDamageByOwner(Fight fight) : int
  * dps(Fight fight) double
  * getDuration(Fight fight) long
  * divideDamageDealtByAbility (Fight fight) : HashMap<String, Integer>
  * etc.
