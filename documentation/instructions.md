

# Insturctions
The project needs java8 version to work
Release 2 [MyParser.jar](https://github.com/sorjjoon/ot-harjoitustyo/releases/tag/week6)

## Configuration

In order to save log files, the user must have a correctly set up database file, currently the application doens't have the functionality to create a new empty database file via the gui, but there is an empty database set up in Myparser/Data 

The location of the database file is irrelevant, as the user is prompted to choose the location when first attempting to access a database


## Startup

The application can be started with

```
java -jar MyParser-1.0-Week6p.jar
```

this is asuming the user has java8 set up as their default version (you can check default version with java -version)
If not, then in place of java you should use the location of the java8 installation, for example

```
/usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -jar MyParser-1.0-Week6p.jar
```

## Analyzing logs

From the application startup window, to start click File and Open File.
From the popup, find the log you want analyzed and click ok
On the left the log is split into fights, and on the menu you can choose a fight based on it's start time

Saving/loading logs is done in the same menu as opening a new file

If you haven't saved/loaded a log yet, you will be prompted to choose the location of a database file

Save File saves the log being analyzed, this will end in an error if you are attempting to save a file which is already found inside the database (or has the same name as one inside)

Load File promps the user to choose a log to be loaded from the logs found inside the database
