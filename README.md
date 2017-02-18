# QuizSystem
AC31007 Coursework

**Note: This project requires Java 8!**

## Building and Running
If you don't have a local Gradle 3.x install, please replace `gradle` with `./gradlew` on Linux/macOS or `.\gradlew.bat`
on Windows to use the wrapper.

- To generate Eclipse project files: `gradle eclipse`
- To run using the embedded Spark server: `gradle run`
- To run on a Tomcat server: `gradle appRun`
- To just build the JAR and WAR files: `gradle build`

Output JAR and WAR files are found in `build/libs`. If you want to test in an environment closely matching the School of
Computing internal server, use `gradle appRun`, as Tomcat is the server used by the school.

## IDEs
- IntelliJ IDEA: Make sure you have the Ultimate edition. Open this directory as a project to start, and use the
settings shown [here](https://github.com/AC31007-Group-8/QuizSystem/blob/master/docs/idea_import.png) (namely, turn off
the seperate source sets option)
- Eclipse: Generate the project files as detailed above, then open that as a project.
- Netbeans: See Erik for help on the Netbeans Gradle plugin.
- Anything else: Edit project files, then use the above Gradle commands manually.

## Developer Notes
Extra documentation can be found in `docs`, including how to use the Database layer.