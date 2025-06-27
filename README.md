
---
# LMT Queue System

A Spring Boot application for managing print job queues, provisioning, and printer gateway integration.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Setup & Installation](#setup--installation)
- [Build & Run](#build--run)
- [Testing](#testing)
- [API Endpoints](#api-endpoints)
    - [PrinterService / PosQSystem](#printersystem--posqsystem)
    - [PrinterGateWaySystem](#printergatewaysystem)
    - [ProvisioningSystem](#provisioningsystem)
- [Postman Collection](#postman-collection)
- [License](#license)

---

## Overview

LMT Queue System is a RESTful service for managing print jobs, printer provisioning, and printer gateway state. It supports enqueueing/dequeueing jobs, updating printer states, and provisioning printer identifiers (lniata).

## Features

- Enqueue and dequeue print jobs
- Retrieve jobs by lniata
- Change printer state (e.g., STOPPED, HELD)
- Provision, update, and delete lniata
- API documented via Postman collection

## Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- Maven
- SQL (database)
- Postman (API documentation/testing)

## Setup & Installation

1. **Clone the repository:**
   ```sh
   git clone <your-repo-url>
   cd <your-project-directory>
   ```

2. **Configure the database:**
    - Update `application.properties` with your DB credentials.

3. **Import Postman Collection:**
    - Use `LMT Queue System - Local.postman_collection.json` in Postman for API testing.

## Build & Run

**Build the project:**
```sh
mvn clean install
```

**Run the application:**
```sh
mvn spring-boot:run
```
or
```sh
java -jar target/<your-jar-file>.jar
```

The server will start at `http://localhost:8080`.

## Testing

**Run all tests:**
```sh
mvn test
```

## API Endpoints

### PrinterService / PosQSystem

#### Enqueue Print Job

- **POST** `/api/v1/enqueue`
- **Body:**
  ```json
  {
    "lniata": "ABC111",
    "data": "D3"
  }
  ```
- **Description:** Adds a print job to the queue for the specified lniata.

---

### PrinterGateWaySystem

#### Retrieve Job from Queue

- **GET** `/api/v1/retrieve/{lniata}`
- **Description:** Retrieves the next print job for the given lniata.

#### Status Change

- **POST** `/api/v1/state-change`
- **Body:**
  ```json
  {
    "lniata": "ABC111",
    "state": "STOPPED"
  }
  ```
- **Description:** Changes the state of the printer (e.g., STOPPED, HELD).

#### Dequeue a Job

- **POST** `/api/v1/dequeue/{lniata}`
- **Description:** Removes the next job from the queue for the given lniata.

---

### ProvisioningSystem

#### Create lniata

- **POST** `/api/v1/provision`
- **Body:**
  ```json
  {
    "lniata": "ABC111"
  }
  ```
- **Description:** Provisions a new lniata (printer identifier).

#### Update lniata

- **PUT** `/api/v1/provision/{lniata}`
- **Body:**
  ```json
  {
    "lniata": "ABC111"
  }
  ```
- **Description:** Updates an existing lniata.

#### Delete lniata

- **DELETE** `/api/v1/provision/{lniata}`
- **Description:** Deletes the specified lniata.

---

## Postman Collection

- The Postman collection file `LMT Queue System - Local.postman_collection.json` is included in the repository.
- Import it into Postman to test all endpoints with sample requests.

## License

This project is licensed under your chosen license.

---

**Note:** Update `<your-repo-url>`, `<your-project-directory>`, and `<your-jar-file>.jar` as appropriate. Add more details as needed for your specific implementation.