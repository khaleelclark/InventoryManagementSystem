# InventoryManagementSystem
This is my DMS Project for Software Development 1 - 31774 course.

The IMS is a **GUI-based Java application** backed by a **SQLite database** that helps users manage their inventory efficiently. It allows adding, removing, updating, and viewing product information either manually or through file import. The system includes robust error handling, confirmation prompts, and useful features to help users monitor stock levels and estimate inventory value.

## Features

- **Product Management**  
  Add, remove, update, and view product details (e.g., name, quantity, price, category, location).

- **Data Import**  
  Load product data from a `.csv` or `.txt` file.

- **Stock Monitoring**  
  View a list of all products below the expected quantity threshold.

- **Inventory Valuation**  
  Calculate the total estimated value of all items in inventory.

- **User Interaction**  
  Clear confirmation dialogs to prevent accidental deletion or overwriting of data.

- **Robust Validation**  
  Informative error messages and field-by-field input validation.

## Technologies Used

- **Java 17**
- **JavaFX** (UI)
- **SQLite** (data storage)

## Requirements

- Java Development Kit (JDK) 17 or later
- JavaFX SDK installed and configured in your IDE
- SQLite database file (`inventory.db`) auto-generates on first run
- *(Optional)* A properly formatted `products.csv` or `products.txt` file:
  
  Example line format:
  Milk,1,3,2.16,FOOD_BEVERAGES,Fridge
  *(Fields: name, quantity, expected quantity, price, category, location)*

## How to Run

### 1. Clone the repository

```bash
git clone https://github.com/khaleelclark/InventoryManagementSystem.git
cd InventoryManagementSystem
```

### 2. Open the project in IntelliJ (or your preferred IDE)

- Ensure JavaFX is configured via **Project Structure > Libraries**
- Add the following **VM options** under **Run > Edit Configurations**:

```bash
--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
```

### 3. Run InventoryManagementSystem.java to start the program


