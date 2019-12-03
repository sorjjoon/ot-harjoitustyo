# Application Logic

There are 2 classes which form most of the functionality of the application, Row and Fight, shown below.  
![architecture](/documentation/images/architecture.png)

In addition to these two, classes Reader and Stats contain static methods for the diffrent services provided by the application

Reader provides the static method  
  * readFile(File file) : ArrayList<Fight>  
 
Which is responsible for reading a given log file (Reader also contain statics methods for determining the name of a file from it's absolute path, and a date from the name of a log. As logs have default names which contain the date)

The class Stats contains numerous static methods for calculating different metrics about a specfic fight, such as
  * getAllDamageByOwner(Fight fight) : int
  * dps(Fight fight) double
  * getDuration(Fight fight) long
  * divideDamageDealtByAbility (Fight fight) : HashMap<String, Integer>
  * etc.  
  

Seperate from these is the Database class which can store the information in a Row object into an sql database, and create Row objects based on the information stored inside a database.

## Core Functionility  

Below is shown as a sequence diagram how the application reads data in a give log file and then displays it to the user.

![file reading](/documentation/images/FileReading.JPG)

First the user interface asks for the user provide the file they wish to analyze (this is not shown on the diagram, to make the diagram eaiser to read). This causes the ui to the before mentioned static readFile(File file) method from the Reader class. Now the readfile method inputs the rawlines in the logfile to the Row constructor, which returns a Row object containing all read information inside. Reader then splits these Rows into Fights, using enterCombat and exitCombat events tracked inside Rows (in adition to exitCombat also death events are used to track end of fights). Reader also combines fights starting/ending close to one another. After the entire log has been read an ArrayList containing all the Fights found inside is returned.  
With this ArrayList, an Analysis object is created  for each fight. Analysis class calculates all stats displayed by the ui (using methods from Stats class), formats values into Strings.  
When the user wants to look at stats for a particular fight, the Ui class updates the view it provides to the user by updating elements on screen to the corresponding Analysis object (and this view is updated each time the user switches the fight they are looking at)
