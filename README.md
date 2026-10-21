# Library Management System

A desktop Library Management System built with **JavaFX**, **FXML** (Scene Builder) and **MySQL**,
following the **MVC** pattern and a **layered architecture**.

## Features

- Login with credentials stored in the database (passwords saved as SHA-256 hashes)
- Dashboard with live statistics (total books, members, borrowed and overdue books)
- Add books with validation and duplicate ID checks
- Register members, then view, search, update and delete them (full CRUD)
- Issue books to members (stock is reduced automatically)
- Return books with overdue detection
- Borrowing history with search and status filter (Borrowed / Returned / Overdue)
- Confirmation, warning and error dialogs using JavaFX `Alert`

## Architecture

```
src/main/java/com/library
├── Main.java          application entry point
├── controller/        Controller layer (MVC) – handles UI events from the FXML views
├── service/           Service layer – business rules and transactions
├── dao/               Data Access layer – DAO interfaces and CrudUtil
│   └── impl/          JDBC implementations of the DAOs
├── db/                DBConnection – singleton MySQL connection
├── model/             Model layer (MVC) – Book, Member, BorrowRecord
└── util/              AlertUtil, Validator, SceneManager, StatusCell

src/main/resources
├── view/              FXML views (MVC View layer, built in Scene Builder)
├── css/style.css      application styling
└── config/db.properties   database connection settings

database/library_db.sql    MySQL schema and sample data
```

Each layer only talks to the layer directly below it:
`View (FXML) → Controller → Service → DAO → MySQL`.

## Requirements

- JDK 21 or newer
- MySQL Server 8
- Maven (or use the included `mvnw` wrapper)

## Setup

1. **Create the database** (creates `library_db`, all tables and sample data):

   ```bash
   mysql -u root -p < database/library_db.sql
   ```

2. **Set your MySQL login** in `src/main/resources/config/db.properties`:

   ```properties
   db.url=jdbc:mysql://localhost:3306/library_db
   db.username=root
   db.password=your_password
   ```

3. **Run the application**:

   ```bash
   ./mvnw clean javafx:run
   ```

## Default login

| Username | Password |
| --- | --- |
| `admin` | `admin123` |

## Database tables

| Table | Purpose |
| --- | --- |
| `users` | Login accounts |
| `books` | Book catalogue and available copies |
| `members` | Registered library members |
| `borrow_records` | Issued and returned books (linked to `members` and `books`) |
