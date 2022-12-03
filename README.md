![banner](https://i.postimg.cc/634y7psn/blob-banner-small.png)

# Backend for QuestPets

# Running

1. Ensure your java version is set to 11 (or higher. Tested on Java 11.0.12)
2. Navigate to `src/main/resources/application.properties` and add the key-values for the database connection as follows (If it doesn’t exist, create it):

```
spring.data.mongodb.uri=mongodb+srv://QuestPets:8UwDqweavCFwA9FF@questpets.dbvntgm.mongodb.net/test
spring.data.mongodb.database=EntitiesDB
server.error.whitelabel.enabled=true
```

1. Navigate to the src/main/java/com/backend folder from a terminal
2. Ensure the port `8080` is free and open for the backend to use
3. Run the command `command_place_holder` on MacOS or `command_place_holder` on Windows
    - You could use your IDE as well to run the QuestPetsApplication.java
4. The API is accessible at `[http://localhost:8080/](http://localhost:8080/)` The root route is a test route to ensure the service is active (HelloWorld)

# Testing

-   Navigate to the `src/test/java` file and run the desired class to be tested using your IDE.
-   This will setup a Spring environment and establish the database connections required for the rest of the test files to work properly.

# Contributing

-   Open the backend directory in your IDE (not the project root folder) as otherwise your IDE may have trouble downloading the Gradle dependencies
-   Follow the pre-existing naming convention (camelCase)

# Debugging

-   An error during the run command is usually either due to the wrong java version or blocked port
-   Debugging information can be traced through logged messaged and following error stackTrace (LogHandler Deprecation disable)
-   The “Whitelabel error” / “Error handling this Response” means you are missing imports specific to Spring or have misconfigured routes in your application

# How It’s Designed

There are 4 packages to note here:

1. `com.backend.controllers`
2. `com.backend.entities`
3. `com.backend.repositories`
4. `com.backend.usecases`

Also to note the directory `error` that provides a statically accessible logger method for the Backend through a custom built logger.

# Flow of Data

```mermaid
flowchart TD
  client(Client) <--> controller(Controller)
	controller <--> manager(Use Case)
	manager <--> model(Entity)
	repository <--> model
	model --> repository(Repository)
	exceptionHandler(LogHandler)
	controller <--> exceptionHandler
	manager --> exceptionHandler


```

## Entities

- Data Transfer Objects
- Perform Validation
- Perform Generation

## Controllers

- Interactor for the API
- Handles API calls from frontend (or client)
- Contracts appropriate manager to complete request

## Repositories

- Handles database connection
- Custom queries
- Saving and deleting data

## Use Cases

- Handles use case requests
- Package raw data to entities
- Contact repositories when needed
- Check for error handling (with LogHandler)
