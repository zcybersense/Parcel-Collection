# Parcel-Collection

A Java-based desktop application designed to simulate parcel and customer processing at a logistics depot. Built with a focus on **Object-Oriented Programming (OOP)** principles and **GUI development** using Java Swing, the system supports customer-queue management, parcel tracking, logging, and fee calculations.

---

## Features

- ✅ Load and display customer and parcel data from CSV files
- ✅ Add and remove customers and parcels dynamically
- ✅ Sort and process parcels in queue
- ✅ Real-time GUI panel updates based on actions
- ✅ Output logs showing parcel processing status
- ✅ Save activity to a log file with timestamps
- ✅ MVC pattern implemented for modular architecture
- ✅ Singleton pattern applied in logging mechanism

---

## Design Patterns

- **Singleton Pattern**: Used in the `Log` class to maintain a single instance for recording all system events
- **MVC (Model-View-Controller)**: Clearly separates model logic, UI rendering, and controller interaction for clean design

---

## GUI Components

- **MainFrame**: Central JFrame to load other panels
- **CustomerPanel**: View, add, or remove customers
- **ParcelPanel**: View, process, or sort parcels
- **Buttons & Tables**: Used for interaction and data presentation

---

## 📂 Project Structure

ParcelCollectionProject/ ├── .idea/ # IntelliJ project config ├── src/ │ └── main/ │ └── java/ │ └── org.example/ │ ├── model/ # Core models: Parcel, Customer, etc. │ ├── service/ # Business logic (e.g., fee calculations) │ ├── ui/ # GUI components (panels, frames) │ └── utils/ # Logging, helpers ├── resources/ │ ├── Custs.csv # Sample customer data │ └── Parcels.csv # Sample parcel data ├── target/ ├── pom.xml └── README.md

![image](https://github.com/user-attachments/assets/71bca0dc-7798-4758-91bb-cf9dccd5f60b)

[2025-01-04 16:42:48] [INFO] Successfully loaded parcel: 'X020' [2025-01-04 16:42:48] [INFO] Successfully loaded parcel: 'X025' ...


---

## 💾 Technologies Used

- Java 21 (OpenJDK)
- Swing (for GUI)
- Maven (via `pom.xml`)
- IntelliJ IDEA
- CSV for input data
- Text file output for logs

---

## 📌 Assignment Requirements Addressed

| Section            | Status |
|--------------------|--------|
| Load + process data from CSV | ✅ Done |
| Log system events            | ✅ Done |
| MVC Architecture             | ✅ Done |
| Singleton Log class          | ✅ Done |
| GUI with dynamic updates     | ✅ Done |
| Fee calculation method       | ✅ Included |
| Reflective Report Ready      | ✅ Optional extension |

---

## Author

Developed by **@zcybersense** as part of an academic assignment focused on applying design patterns, OOP, and file I/O with GUI development.

---

## How to Run

1. Open the project in IntelliJ IDEA
2. Build using Maven or the IDE build tools
3. Run `Main.java` to launch the GUI
4. Make sure the `.csv` files are located under the `resources/` folder

---

## License

This project is for educational purposes and may be adapted for academic submissions or personal portfolio use.

