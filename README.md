# Development

Development repository for LO23 music sharing application.

For Gitlab worfkow, coding conventions and other documentation, please refer to the [wiki](https://gitlab.utc.fr/lo23-a19-mardi/development/wikis/home).

## Requirements
- [Java Development Kit 1.8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) : 
    - if you use an IDE, make sure to set the project to run with JDK 1.8
    - if you have more than one JDK versions on your computer, make sure JDK 1.8 is the default one
- [Maven](http://maven.apache.org/download.cgi)

## To run the project
```bash
$ mvn clean install
$ mvn exec:java -pl main -Dexec.mainClass=Main
```

## To check the style
```bash
$ mvn checkstyle:check
```
