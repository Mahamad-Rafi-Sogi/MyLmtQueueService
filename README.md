🧾 LMT Queue System
The LMT Queue System is a lightweight and extensible queue management service designed to handle message ordering and processing with precision. It is built with a custom doubly-linked list logic stored in a relational database (e.g., MySQL), enabling queue operations like enqueueing, dequeuing, and traversing in a persistent and scalable manner.

🏗️ Architecture Overview
LMT_QUEUE Table
Represents a queue for a specific LNIATA (identifier).
Contains:

HEAD_ID: Points to the first element in the queue.

TAIL_ID: Points to the last element in the queue.

STATE: Queue state (e.g., ACTIVE, INACTIVE).

PRINTER_GATEWAY_URL: Optional URL for processing integration.

LMT_QUEUE_ELEMENT Table
Represents individual elements in the queue, linked with:

PREVIOUS_ID and NEXT_ID (forming a doubly-linked list).

DATA: Payload (stored as hex-encoded data).

📌 Features
Persistent queue backed by a relational database

Doubly linked list structure enables fast inserts and removals

Supports multiple queues identified by LNIATA

Designed for use in asynchronous or batch message processing pipelines

Easily integrates with external printer gateways or processing services

📩 Sample Entry
-- Queue
ID | LNIATA | STATE   | HEAD_ID                             | TAIL_ID                             | PRINTER_GATEWAY_URL
---|--------|---------|--------------------------------------|--------------------------------------|----------------------
2  | 67890  | INACTIVE| 550e8400-e29b-41d4-a716-446655440001| 550e8400-e29b-41d4-a716-446655440002| null

-- Elements
ID                                   | LNIATA | DATA                              | PREVIOUS_ID                           | NEXT_ID
------------------------------------|--------|-----------------------------------|---------------------------------------|---------------------------------------
550e8400-e29b-41d4-a716-446655440001| 67890  | 53616d706c6520446174612032       | null                                  | 550e8400-e29b-41d4-a716-446655440002
550e8400-e29b-41d4-a716-446655440002| 67890  | 353336313664373036633635...      | 550e8400-e29b-41d4-a716-446655440001 | null

🚀 Tech Stack
Java + Spring Boot
MySQL / MariaDB
JPA / Hibernate

REST API for queue operations

📚 Endpoints (examples)
Method	Endpoint	Description
POST	/emqueue	Enqueue new data element
GET	/queue/{id}	View queue and its state
DELETE	/dequeue	Remove head of the queue
