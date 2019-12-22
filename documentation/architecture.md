## Application Logic
Classes in the domain package (Row and Fight) are as used as wrappers to pass information from events inside between diffrent classes about the log being analyzed (Row represent a single line in a log file, and Fight represents are Rows that belong to a single in-game fight), so all classes (apart from ui, which uses these via the Analysis class) have some of their functionality dependant on these two classes, most importantly the abstract classes Stats and Reader. Stats contains all methods used for the calculation of stats. Reader is responsible for turning the information in the log txt file into Fights. All methods in Stats and Reader have been made static. Making Fight and Reader the only two objects used by the application logic.


Row and Fight, shown below.  
![architecture](/documentation/images/architecture.png)

In addition to these two, classes Reader and Stats contain static methods for the diffrent services provided by the application

Reader provides the static method  
  * readFile(File file) : ArrayList<Fight>  
 
Which is responsible for reading a given log file

The class Stats contains numerous static methods for calculating different metrics about a specfic fight, such as
  * dps(Fight fight) double
  * getDuration(Fight fight) long
  * divideDamageDealtByAbility (Fight fight) : HashMap<String, Integer>
  * etc.  
  
  
The Ui package contains 2 important classes, GuiController and Analysis, others are classes in the package are the main class (and Gui , which creates a Scene using GuiController). The ColumnData class is a simple wrapper for Guicontroller to use for inputing data into the Ability usage tab tableView.  

The Analysis object is a wrapper for all information shown on the Gui page, formated into Strings (in a few cases data is Integers or Doubles, such as for chart data). GuiController contains all the logic behind the gui page and handles all user input. (See core functionality for more details, and Analysis javadoc for all elements found in the object). 

Seperate from these is the Database class (in the database package) which can store the information in a Row object into an sql database, and create Row objects based on the information stored inside a database. This class also provides a few utility methods, such as reset() for creating an empty database file, and getSavedLogsAndMessages() which retrieves all log names and, messages users attached to the log names.  
Database implements the LogStorage interface (and is used by the ui via this interface). LogStorage itself implements closable (so Database can be used be try with), and defines the three methods used by the interface, reset(), getSavedLogsAndMessages(), getFightsFromLog(String logName) ArrayList<Fight> , which retrieves all Fight belonging to a certain log name and addListOfFights(List<Fight> fights, LocalDate date, String type, String logFileName), which saves the given list of Fight objects into the database.




## Core Functionility  

Below is shown as a sequence diagram how the application reads data in a give log file and then displays it to the user.

![file reading](/documentation/images/FileReading.JPG)

First the user interface asks for the user provide the file they wish to analyze (this is not shown on the diagram, to make the diagram eaiser to read). This causes the ui call the before mentioned static readFile(File file) method from the Reader class. Now the readfile method inputs the rawlines in the logfile to the Row constructor, which returns a Row object containing all read information inside. Reader then splits these Rows into Fights, using enterCombat and exitCombat events tracked inside Rows (in adition to exitCombat also death events are used to track end of fights). Reader also combines fights starting/ending close to one another. After the entire log has been read an ArrayList containing all the Fights found inside is returned.  
With this ArrayList, an Analysis object is created  for each fight. Analysis class calculates all stats displayed by the ui (using methods from Stats class), and formats these values into Strings.  
When the user wants to look at stats for a particular fight, the Ui class updates the view it provides to the user by updating elements on screen to the corresponding Analysis object (and this view is updated each time the user switches the fight they are looking at)

When reading from a database the data flow would be exactly the same, but the arrayList of fights is created by the Database class instead of the abstract Reader (which the ui uses via the LogStorage interface)
