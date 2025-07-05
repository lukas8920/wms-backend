# wms-backend

**wms-backend** is a backend service designed to support integration with Odoo's Warehouse Management System (WMS), with a current focus on importing sales orders. The system is implemented in Java and follows a modular, service-oriented architecture for extensibility and maintainability.

## Features

- **Sales Order Import:**
    - Accepts new sales orders via a REST API endpoint (`/v1/api/saleorder`).
    - Each sales order includes a partner reference, an external client order reference, and one or more sales lines (products/quantities).
    - Validates partner and product existence before creating orders.
    - Ensures sale order references are unique; prevents duplicates.
    - Handles the creation of sale orders and associated sale lines in Odoo via API calls.
    - Updates the status of newly created sale orders.
    - Rolls back all changes if any part of the creation process fails, ensuring data consistency.
- **Error Handling and Logging:**
    - Provides detailed error responses for invalid requests or system errors.
    - All operations are logged for traceability.

## Technology Stack

- **Language:** Java
- **API Framework:** Spring Boot
- **Build Tool:** Maven
- **Integration:** Odoo (via custom API classes)
- **Testing:** JUnit with Mockito for service layer tests

## API Overview

- **POST /v1/api/saleorder**
    - Submits a new sales order for import.
    - Requires JSON payload with partner, clientOrderRef, and lines.

### Example Request

```json
POST /v1/api/saleorder
{
  "partner": "PartnerNameOrID",
  "clientOrderRef": "ExternalOrder123",
  "lines": [
    {
      "product": "ProductSKU",
      "qty": 10
    }
  ]
}
```

## Development & Contribution

At this stage, the project’s main functionality is importing and persisting sales orders. Future plans may include support for additional document types and warehouse operations.

Contributions are welcome via pull requests and issues. Please ensure you add tests for any new features or bug fixes.