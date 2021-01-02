# Ticketline

## Description
This repository contains the code created during the *Software Engineering & Project Management* course by our group during the summer term 2017.
The [description](documents/description) folder contains the description of the project as given by the TU Wien.

### Brief summary of the project
*Ticketline* is an application to manage and sell tickets for various events. The application is bi-lingual (DE & EN) and provides different views depending on the user that is logged-in: *Admins* can manage all events, tickets and bills whereas *Users* are limited to browse, filter, search for events and reserve & buy tickets or cancel a reservation/purchase.

---

## Building Ticketline

1. Install:
    * Oracle JDK 1.8.0_112+
2. Navigate to the ticketline code directory
3. Build the server as well as the client with the following commands

### Server + Client:
```
./mvnw clean verify
```
### Server:
```
./mvnw -pl=server -am clean verify
```
### Client
```
./mvnw -pl=client -am clean verify
```
---

## Running Ticketline

1. Navigate to the ticketline code directory
2. Run the server as well as the client with with the following commands

### Server:
```
./mvnw -pl=server -am spring-boot:run
```

To generate some testData add the generateData profile when running the server
```
./mvnw -pl=server -am spring-boot:run -Drun.profiles=generateData
./mvnw -pl=server -am spring-boot:run -Drun.profiles=generateData,development
```
On Windows Powershell
```
./mvnw -pl=server -am spring-boot:run '-Drun.profiles=generateData'
./mvnw -pl=server -am spring-boot:run '-Drun.profiles=generateData,development'
```

### Client
```
./mvnw -pl=client -am spring-boot:run
```

## Login

You can login to the client using one of the following credentials:

* System Administrator
  * Username: admin
  * Password: password
* User
  * Username: user
  * Password: password
* Database
  * Username: ticketline
  * Password: ticketline

---

## Intellij Configuration

For a smooth programming feeling you should install the following plugin:
https://github.com/mplushnikov/lombok-intellij-plugin

## Versioning

use the following command to create versions:

    mvn versions:set -DnewVersion=1.0
    git add .
    git commit -m "bump to version 1.0"
    git tag v1.0
    git push origin dev --tags
    
## Authors
calvinclaus, cglantschnig, yungfred, Akkes1 & martin-kl
