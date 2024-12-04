# Receipt Processor

This project is a take home coding challenge provided by Fetch Rewards. It is a backend web service that exposes two API endpoints:
- **POST /receipts/process**: Submit a receipt for processing.
- **GET /receipts/{id}/points**: Retrieve the points associated with a specific receipt.

---

## Features

- Processes receipt JSON payloads and calculates points based on predefined rules.
- Returns a unique receipt ID for tracking.
- Supports retrieving the calculated points for a given receipt ID.
- Dockerized setup for easy deployment.
- Includes a `docker-compose.yml` file for simplified container management.

---

## Technologies

- **Java 21**
- **Spring Boot 3.4.0**
- **Maven**
- **Docker**
- **Docker Compose**

---

## Setup Instructions

### Prerequisites

If you are using **Docker**, you only need:
- **Docker** and **Docker Compose**

If you are using **Docker Compose**, you also need:
- **Docker Compose**, which is included in modern versions of Docker Desktop

If you are running the application locally (without Docker), you will need:
- **Java 21**
- **Maven**

### Running With Docker Compose

1. **To Start**
In your terminal run:
```
docker-compose up
```
  
2. **Access the Application**
- The application will be available at http://localhost:8080 which you may use a baseURL in Postman

3. **To End**
In your terminal run:
```
docker-compose down
```

### Running With Docker

1. **Build and Run the Docker Container**
In your terminal run:
```
docker build -t receipt-processor .
docker run -d --name receipt-processor -p 8080:8080 receipt-processor
```

2. **Access the Application**
- The application will be available at http://localhost:8080 which you may use a baseURL in Postman

3. **To End**
In your terminal run:
```
docker stop receipt-processor
docker rm receipt-processor
```

### Running Locally (Without Docker)

1. **Clone the Repository**
In your terminal run:
```
git clone git@github.com:SarahEGreer/receipt-processor-challenge.git
cd receipt-processor-challenge
```

2. **Build Project**
In your terminal run:
```
mvn clean package
```

3. **Run the Application**
In your terminal run:
```
java -jar target/receipt-processor-0.0.1-SNAPSHOT.jar
```

4. **Access the Application**
- The application will be available at http://localhost:8080 which you may use a baseURL in Postman

---

## API Endpoints

### **1. Process Receipts** 

- **Endpoint**: `/receipts/process`
- **Method**: `POST`
- **Description**: Submits a receipt for processing and returns a unique receipt ID.

#### **Request Payload** 
```json
{
  "retailer": "Retailer Name",
  "purchaseDate": "2022-01-01",
  "purchaseTime": "14:33",
  "total": "9.00",
  "items": [
    {
      "shortDescription": "Item description",
      "price": "5.00"
    }
  ]
}
```

### **Response**

- **Status Code**: `200 OK`
- **Response Body**:
```json
{
  "id": "unique-receipt-id"
}
```

### **2. Get Points**

- **Endpoint**: `/receipts/{id}/points`
- **Method**: `GET`
- **Description**: Retrieves the number of points awarded for the specified receipt ID.
- **Path Parameter**: `id:` The unique receipt ID returned by the `/receipts/process` endpoint.

### **Response**

- **Status Code**: `200 OK`
- **Response Body**:
```json
{
  "points": 100
}
```

### **Error Response**

- **Status Code**: `404 Not Found`
- **Response Body**:
```json
{
  "error": "No receipt found with ID: `provided-id`"
}
```

---

## API Specification

This project follows the OpenAPI 3.0 specification to document the API. The full API definition is available in the `api.yml` file.

### File Location
The `api.yml` file is located in the `src/main/resources/` directory.

### How to Use
1. Open the `api.yml` file to view the API specification.
2. You can use the file with tools like:
   - **Postman**: Import the `api.yml` file into Postman to set up collections and test the API endpoints.

---

## Point Allocation Rules

The following rules define how points are awarded for each receipt:

1. **Retailer Name**:
   - **1 point** for every alphanumeric character in the retailer's name.

2. **Total Amount**:
   - **50 points** if the total is a round dollar amount with no cents (e.g., `$9.00`).
   - **25 points** if the total is a multiple of `$0.25`.

3. **Items**:
   - **5 points** for every two items on the receipt.
   - If the trimmed length of an item's description is a multiple of 3:
     - Multiply the item's price by `0.2` and round up to the nearest integer.
     - The result is the number of points awarded for that item.

4. **Purchase Date**:
   - **6 points** if the day of the purchase date is odd.

5. **Purchase Time**:
   - **10 points** if the time of purchase is after 2:00 PM and before 4:00 PM.

### **Example Calculation**

**Sample Receipt**:
```json
{
  "retailer": "Target",
  "purchaseDate": "2022-01-01",
  "purchaseTime": "13:01",
  "items": [
    {
      "shortDescription": "Mountain Dew 12PK",
      "price": "6.49"
    },
    {
      "shortDescription": "Emils Cheese Pizza",
      "price": "12.25"
    },
    {
      "shortDescription": "Knorr Creamy Chicken",
      "price": "1.26"
    },
    {
      "shortDescription": "Doritos Nacho Cheese",
      "price": "3.35"
    },
    {
      "shortDescription": "   Klarbrunn 12-PK 12 FL OZ  ",
      "price": "12.00"
    }
  ],
  "total": "35.35"
}
```
### **Point Breakdown**

- **6 points**: Retailer name ("Target") has 6 alphanumeric characters.
- **10 points**: 5 items (2 pairs @ 5 points each).
- **3 points**: "Emils Cheese Pizza" has 18 characters (multiple of 3); `12.25 * 0.2 = 2.45` (rounded up to 3 points).
- **3 points**: "Klarbrunn 12-PK 12 FL OZ" has 24 characters (multiple of 3); `12.00 * 0.2 = 2.4` (rounded up to 3 points).
- **6 points**: Purchase day (1st) is odd.

**Total Points**: `28`

---

## Testing

This project includes the following types of tests to ensure functionality and reliability:

### **1. Unit Tests**
- **Purpose**: Validate the correctness of individual components, such as utility methods for calculating points.
- **Examples**:
  - Tests for the `PointsCalculator` class, including:
    - Calculating points for alphanumeric characters in retailer names.
    - Handling edge cases like null or empty fields in receipts.
    - Validating rules such as round dollar amounts, item description lengths, and purchase times.

### **2. Service Tests**
**Purpose**: Ensure that the service layer behaves as expected and handles receipt processing logic correctly.

**Examples**:
- Tests for the `ReceiptService` class to verify:
    - Receipts with valid data are stored and processed correctly.
    - Requests with invalid data throw appropriate exceptions, such as:
        - `InvalidReceiptException` for receipts with missing or empty items.
        - `ReceiptNotFoundException` for non-existent receipt IDs.
    - Points are accurately calculated using the service logic.

### **3. Integration Tests**
- **Purpose**: Verify the interaction between components, focusing on API endpoints.
- **Examples**:
  - Tests for the `ReceiptController` to ensure:
    - Receipts can be processed successfully via `POST /receipts/process`.
    - Points can be retrieved for valid receipt IDs via `GET /receipts/{id}/points`.
    - Appropriate error responses (e.g., `404 Not Found`) are returned for invalid receipt IDs.

### **4. Edge Case Testing**
- **Purpose**: Test unusual or invalid inputs to ensure the application handles them gracefully.
- **Examples**:
  - Receipts with missing or invalid fields (e.g., null or improperly formatted dates).
  - Receipts with empty or zero items.

### **Running Tests with Docker Compose**

In your terminal run:
```
docker-compose run --rm test
```


### **Running Tests Locally**

If you have Maven installed on your machine, you can run the tests locally:
In your terminal run:
```
   mvn test  
```

---

### Contact

Please [contact me](https://sarah-greer-portfolio.netlify.app/#contact) if you have questions about my code.
