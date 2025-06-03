Docker Management Server
---

### Setup an IDE
1. get the JDK 21 (Choose whatever the flavor you wish)
2. Clone the repository
3. Open Intellij and click on `File -> New -> Project from Existing Sources`
4. Choose `build.gradle` from cloned repo.
5. Ensure everything is using java 21
   1. Set Project JVM to java 21
      1. Open project structure window (`File` then `Project Structure`)
      2. Ensure `Project SDK` is java 21
      3. ensure `Language Level` is 21
   2. Ensure gradle uses Java 21
      1. Navigate to settings wundow (`File` then `Project Structure`)
      2. `Build, Execution, Development` -> `Build Tools` -> `Gradle`
      3. Ensure Gradle JVM set to `Use Project JDK`
6. Setup project Lombok.
7. By default, the application uses `h2` in-memory database
   1. you can access the database at `http://localhost:9090/h2-console`
   2. you can replace the JDBC URL with the one from `application.yml` and login
   3. if you wish you can connect to any database by replacing datasource config with your own DB config
8. Added run configuration to start the server, you can use that to start server


   