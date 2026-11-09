# ADP - Capstone Project
## KN Group 6
### Group Members:
- Angel Dineo Masonganye (223008869)
- Ryle Peter May (230333907)
- Litha Owethu Mazibuko (240143485)
- Nompumelelo Mbatha (240256727)
- Ntombozuko Palisa Mchophele (240455789)
- Yamkela Mgcubhe (222040114)

# Freight and Delivery Management System

A courier management system. Staff manage customers, drivers, vehicles, routes, orders and shipments; drivers update the shipments on their routes; customers place, pay for, track and cancel their own orders.

## Features

**Customers**
- Sign up and log in on their own login page
- Get a price quote on the home page (R50 base fee + R10 per kg)
- Place an order (pickup and delivery address, weight, description, delivery date); a shipment is created automatically
- Pay for an order (simulated payment, no real money is taken)
- Track a shipment through its history timeline
- Cancel an order that has not been picked up yet (a paid order is marked refunded)
- Print a receipt for a paid order
- Edit their profile and change their password

**Drivers**
- See only their own routes and the shipments on them
- Update a shipment's status with a location and a note

**Admin**
- Dashboard with totals, revenue, and orders and shipments by status
- Create, edit, search and delete customers, drivers, vehicles, routes, orders and shipments
- Create customers and drivers together with their login
- Manage user accounts (create, enable or disable, reset password, delete)
- View the full tracking history of any shipment

**Behind the scenes**
- Role-based security (ADMIN, DRIVER, CUSTOMER) with ownership checks, so users only reach their own data
- Server-generated IDs, searchable dropdowns instead of typed IDs
- Shipment status rules (for example a delivered shipment cannot change again)
- Safe deletes: records that are still in use return a clear message instead of an error

## Tech Stack

- Vue 3, vue-router, axios, Vite
- Java 21
- Spring Boot 4.1 (Web, Data JPA, Security)
- MySQL
- Maven

## Prerequisites

- Java 21 and Maven (IntelliJ IDEA handles both)
- MySQL running locally
- **Node.js** (so you have access to `npm`)


## Getting Started

### 1. Database

1. Create an empty MySQL database named `freightanddelivery`.
2. Open `src/main/resources/application.properties` and set your MySQL **username** and **password**.

Hibernate creates the tables automatically the first time the back end starts.

### 2. Back end

1. Open the project in IntelliJ and run the `Main` class.
2. The API starts on port 8080 under `http://localhost:8080/freightanddelivery`.
3. On the first start an administrator account is created automatically if none exists.

### 3. Front end

1. Open a terminal (Command Prompt or IntelliJ's terminal) and `cd` into the `frontend` directory.
2. Run:

   `npm install`

   This installs all the dependencies needed to run the project.
3. Run:

   `npm run dev`

4. Open `http://localhost:5173`.

If the back end is not on `localhost:8080`, set `VITE_API_BASE_URL` before starting the front end.

## Running The Application

There are two separate ways in: one for customers and one for staff.

| Page | Path |
| --- | --- |
| Home page | `localhost:5173/home` |
| Customer login | `localhost:5173/login` |
| Customer sign-up | `localhost:5173/register` |
| Staff portal login (admin and drivers) | `localhost:5173/portal-x7q2/login` |

The staff portal login is not linked from the home page on purpose. Go to it directly through the address bar.

**Admin login credentials (created on first start):**
- Username: `admin`
- Password: `admin`

These are development defaults. They can be overridden with the environment variables `APP_ADMIN_USERNAME` and `APP_ADMIN_PASSWORD`. If your database already contained an admin account, that account keeps its old password.

**Customers:** sign up at `/register`, or have an admin create one under Customers.

**Drivers:** an admin creates them under Drivers, entering a username and password. The driver then logs in through the staff portal and sees `My Routes`.

### Test it Out
1. Log in as admin and create a driver, a vehicle and a route.
2. Sign up as a customer, then place an order and pay for it.
3. Log in as the driver and update the shipment's status with a location.
4. As the customer, track the shipment, then open the receipt.
5. As admin, open the dashboard and a shipment's history.

## Project Structure

```
src/main/java/za/ac/cput/
  config/       security configuration, first-admin setup
  controller/   REST endpoints (staff CRUD, /my self-service, registration, quote, stats)
  domain/       Entities
  dto/          request and view records
  repository/   Spring Data repositories
  security/     logged-in user principal and loader
  service/      one folder per entity, plus shared rules (pricing, status rules)
frontend/src/
  views/        pages (admin, customer, driver, public)
  components/   shared pieces such as the side panel and searchable dropdown
  services/     axios client and one service per entity
  router/       routes and role guards
  stores/       login state
```

## Documentation

The full project documentation (process, design, testing, findings, and endpoint reference) is provided with the submission.

## UML Diagram
<img width="980" height="870" alt="ADP-UML" src="https://github.com/user-attachments/assets/d461c1a4-139d-430e-a0bf-56f77c68d9c6" />
