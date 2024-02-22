# MarketJ ![Coverage](.github/badges/jacoco.svg)

## Requisites
* IntelliJ IDEA's Lombok [plugin](https://plugins.jetbrains.com/plugin/6317-lombok-plugin) for easy manipulation of boilerplate code using Lombok annotations. Check it's documentation [here](https://plugins.jetbrains.com/plugin/6317-lombok-plugin).

## Development environment configuration
Whether you are using IntelliJ IDEA or some other coding tool, you need to set the following environment variables:

    ### DATABASE RELATED VARIABLES (default values)###
    MJ_MONGODB_DBNAME=(APITests)
    MJ_MONGODB_PORT=(27017)
    MJ_MONGODB_USER=(api)
    MJ_MONGODB_PASSWORD=(api)
    MJ_MONGODB_AUTH_DB=(admin)

    MJ_API_BACKEND_PORT=(8080)
    MJ_API_DEBUG_PORT=(5005)

## Database configuration

### Setup MongoDB in a Docker container

If you follow this steps you will install and run MongoDB service on an isolated environment:

1- Install docker and docker-compose on your host:

    $ sudo apt install docker.io docker-compose

Note: please make sure you install `docker.io`, not `docker` package

2- Define all necessary environment variables to deploy the MongoDB database system:

    $ export MONGODB_USER=<usename>
    $ export MONGODB_PASS=<password>
    $ export MONGODB_DATABASE=<database_name>

3- Build and start a docker container from docker-compose.yml definition:

    $ docker-compose up -d db

>*Note*: This command will create and configure the necessary docker container for our MongoDB system.
This also will map the MongoDB container port 27017 to your host system, so, in order to run this right
you must have this port available.

## Running the application

### Using IntelliJ IDEA SpringBoot configuration
Select Edit Configurations, and create a new Spring Boot configuration. In the main class field put: com.market.Application
and add the environment variables explained before with your MongoDB configuration. Hit run.

### Using docker-compose

    > docker-compose up -d app

### Using maven
To start the web application, issue the following Maven command on a shell to generate the jar of the project:

    > mvn clean package

and from the console run java -jar /path/to/.rar

### Or

    > mvn spring-boot:run
