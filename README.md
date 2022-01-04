# TJV Semestral project - School enrollment

Semestral project of Raúl Balanzá García for BIE-TJV @ FIT-CVUT on Winter semester 2021-22.

This project is an application to manage enrollment of students in a school and also to store information about which teachers are teaching each course, and which is the schedule of the course.

## API documentation

The API documentation of the application can be found [here](https://documenter.getpostman.com/view/6226958/UVC5F82s#3207cbd9-ef32-4792-bf13-efad4e7e9f6e).

## Deployment details

### Server

The server part of this project is meant to be deployed using Docker Compose. To launch the server, the following steps must be followed:
1. `cd` into the `server` directory
2. Execute `docker-compose up`

### Client

The client part is meant to be deployed using Docker. To launch the client, the following steps must be followed:
1. `cd` into the `client` directory
2. *Optional*: change the backend endpoint in the `src/main/resources/application.properties` file
3. Create the Docker image by executing `docker build -t se_client .`
4. Run a container by executing `docker run -dp <port>:8081 se_client`
   1. `<port>` must be replaced by the desired client port.

## Auto-deployment

This repository is configured to perform automated testing in both the client and server part, and also to deploy automatically the server part into the Oracle Cloud services.

The public API is available at **http://tjv.raulbalanza.me:80**