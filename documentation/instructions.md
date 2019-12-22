

# Insturctions
The project needs java8 version to work  
Release 2 [MyParser.jar](https://github.com/sorjjoon/ot-harjoitustyo/releases/tag/week6)

## Configuration

In order to save log files, the user must have a correctly set up database file, an empty database file can be created from the "Database" menu button. Note previous versions of a database file will not work in the current version. 


## Startup

The application can be started on the commandline with

```
java -jar MyParser-Loppupalautus.jar
```

this is asuming the user has java8 set up as their default version (you can check default version with java -version)
If not, then in place of java you should use the location of the java8 installation, 

```
/usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -jar MyParser-1.0-Week6p.jar
```

## Analyzing logs

From the application startup window, to start click File and Open File.
From the popup, find the log you want analyzed and click ok.
On the left the log is split into fights, and on the list you can choose a fight based on it's start time by double clicking

All availabe stats can be accessed via the tabs next to the fight list

"Overview" tab gives a general overview of statistic for the whole duration of the fight

"Damage" tab contains stats about the damage dealt by the player. From the choice boss on top of the view, the user can choose if he wishes to look at dmg dealt to all targets, or a specfic target
"Healing" and "Damage Taken" tabs contain similar stats for, but for healing and damage taken. In the samy the user can choose to look at all targets (or sources of damage, in case of damage taken), or just one in particular
"Ability Usage" tab has an overview on the users usage of abilites. Abilites with only 1 usage have their avg, max and min stats defaulted to 0

When saving/loading logs, if you haven't connected to a datbase yet, you will be prompted to choose the location of a database file. Note, the application will remember this connection for the duration of the session, so all future saving or loading will be directed to this database file. If you wish to save to a diffrent file, choose "Connect to new Datbase" from the Database menu (on the upper left)

Saving/loading logs is done in the same menu as opening a new file

Save File saves the log being analyzed, this will end in an error if you are attempting to save a file which is already found inside the database (or has the same name as one inside). Upon saving you will be asked if you want to leave a small note with your log

Load File promps the user to choose a log to be loaded from the logs found inside the database

