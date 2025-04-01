# JDBC Lab: Enhanced Library Management System

## Objective
In this lab, you will enhance the **Library Management System** by implementing advanced database features using JDBC. You will integrate **HikariCP** for efficient connection pooling, add SLF4J for logging, introduce update and delete operations, and establish relationships between authors and books.

## Scenario
A local library needs a more robust system to manage books and authors. Your task is to extend the existing Library Management System to support additional functionality and optimize database interactions.

## Setup
### 1. Create the Database
```sql
CREATE DATABASE library_db;
```

### 2. Create the Books Table
```sql
CREATE TABLE books (
    book_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author_id INT NOT NULL,
    publication_year INT NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (author_id) REFERENCES authors(author_id)
);
```

### 3. Create the Authors Table
```sql
CREATE TABLE authors (
    author_id SERIAL PRIMARY KEY,
    author_name VARCHAR(255) NOT NULL,
    books_sold INT DEFAULT 0,
    is_best_seller BOOLEAN DEFAULT FALSE
);
```

## Tasks
### Task 1: Integrate Connection Pooling
- Implement **HikariCP** for database connection pooling.
- Add **SLF4J** to your dependencies for logging purposes.
- Configure the connection pool settings appropriately.

### Task 2: Implement CRUD Operations
- Add a method to **update** book information (title, publication year, availability, etc.).
- Add a method to **delete** a book from the database.

### Task 3: Establish Author-Book Relationship
- Modify the `books` table to reference the `authors` table using a **one-to-many** relationship.
- Update existing methods to insert books with an associated author.

### Task 4: Retrieve Books by Author
- Implement a method to **fetch all books by a specific author**.
- Modify existing book retrieval methods to include **author information**.

### Task 5: Retrieve Books by Best-Selling Authors
- Implement a method to **retrieve books written by best-selling authors**.

## Bonus Challenges
1. **Implement Pagination**: Enhance book retrieval to support pagination.
2. **Sort Books Dynamically**: Allow users to specify sorting order dynamically (e.g., by title, year, availability).
3. **Advanced Search**: Enable filtering by title, author, and publication year range.

