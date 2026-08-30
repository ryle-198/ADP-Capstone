# ADP - Capstone Project
## KN Group 6
### Group Members:
- Angel Dineo Masonganye (223008869)
- Ryle Peter May (230333907)
- Litha Owethu Mazibuko (240143485)
- Nompumelelo Mbatha (240256727)
- Ntombozuko Palisa Mchophele (240455789)
- Yamkela Mgcubhe (222040114)

# Term 3 Deliverable Information

---

For this phase of the capstone project, we created a frontend application to consume our RESTful web services.

## Tech Stack

- Vue.js
- Java 21
- Spring Boot (Web, SQL, Security)

## Prerequisites
The zip file should have the node-modules folder but incase it doesn't, ensure your machine has **Node.js** installed, so you have access to **npm**.

Ensure to set your add your username and password in the application.properties file
## Getting Started

1. Once Node.js is installed and npm is available, unzip the project files.
2. Open a terminal (Command Prompt or IntelliJ's terminal) and `cd` into the `frontend` directory.
3. Run:
    
    `npm install`
   This installs all the dependencies needed to run the project.

## Populating the Database

The `Main` class implements `CommandLineRunner`, which populates the database with sample data automatically when the application starts.

If no data appears after startup, populate it manually:
1. Open the test folder.
2. Run each entity's service test `create` method to insert data.

## Running The Application

The first thing that you want to see is the admin login. There is a homepage but the admin login is the route that leads to the admin section where we make use of our restful services so homepage is just for show for now.



paths:

`localhost:5173/home`

`localhost:5173/admin/portal-x7q2/login`



if on homepage and you wish to got to login: click on login in the navbar or

navigate to `localhost:5173/admin/portal-x7q2/login` through the search bar.

**Admin login credentials:**
- Username: `admin`
- Password: `admin`

## UML Diagram
<img width="980" height="870" alt="ADP-UML" src="https://github.com/user-attachments/assets/d461c1a4-139d-430e-a0bf-56f77c68d9c6" />
