# ActivityCalendarApi
An API that extends the Google Calendar API to add the possibility to categorize events into different activities. This was a simple university course project.

Documentation: https://meysam-mfard.github.io/ActivityCalendarApi/



In order to access and use locally in a project, the created jar file can be installed in the local .m2 maven repository using:
```
mvn install:install-file -Dfile=target/ActivityCalendarApi-1.0-SNAPSHOT.jar -DpomFile=pom.xml
```
https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html

Referencing the local repository afterwards:
```
<dependency>
    <groupId>se.lnu.advsd.meysam</groupId>
    <artifactId>ActivityCalendarApi</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
