# 🚍 Safe School Track System

A comprehensive Java console application ensuring the safety of schoolchildren during transit between home and school. The system provides real-time status tracking, secure communication, and efficient data management among parents, drivers, and educators.

## ✨ Features

- 📍 **Status Tracking** – Drivers update pickup/drop-off status; parents see real-time updates.
- 📋 **Absence Management** – Parents can notify drivers of absences via status messages.
- 💬 **Messaging** – Parents and drivers can send predefined or custom messages through the system.
- 👤 **Account Management** – Create, update, and delete parent and driver accounts.
- 📧 **Email Notifications** – Sends a welcome email on account creation via Gmail SMTP.
- 🔒 **Secure Database** – SQLite with prepared statements for SQL injection protection.

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| ☕ Java 21 | Core language |
| 📦 Maven | Build & dependency management |
| 🗄️ SQLite | Lightweight relational database |
| 📧 Jakarta Mail | Email notifications |
| 🔐 dotenv-java | Secure credential management |

## 📁 Project Structure

```
Safe-School-Track/
├── src/main/java/
│   ├── Main.java                 # 🚀 Entry point
│   ├── User.java                 # 🧬 Abstract base class (OOP)
│   ├── ParentManager.java        # 👨‍👩‍👧 Parent entity & operations
│   ├── Driver.java               # 🚐 Driver entity & operations
│   ├── StudentManager.java       # 🎓 Student status management
│   ├── DatabaseManager.java      # 🗄️ All database CRUD operations
│   ├── LoginManager.java         # 🔑 Authentication & menu system
│   ├── CreateNewUser.java        # ✏️ Account registration flow
│   └── NotificationManager.java  # 📬 Email service
├── Database/
│   └── Database.db               # 💾 SQLite database (gitignored)
├── Credential.env                # 🔐 Email credentials (gitignored)
├── Credential.env.example        # 📄 Template for credentials
├── pom.xml                       # ⚙️ Maven configuration
└── .gitignore
```

## 📋 Prerequisites

- ☕ **Java 21** or higher
- 📦 **Maven 3.9+**

## 🚀 Setup & Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/Safe-School-Track.git
   cd Safe-School-Track
   ```

2. **Set up credentials** 🔐

   Copy the example file and fill in your Gmail app password:
   ```bash
   cp Credential.env.example Credential.env
   ```
   ```
   EMAIL_USERNAME=your-email@gmail.com
   EMAIL_PASSWORD=your-app-password
   ```

3. **Build and run** ▶️
   ```bash
   mvn compile exec:java
   ```

## 🧠 OOP Principles

- 🔗 **Inheritance** – `ParentManager` and `Driver` extend abstract `User` class.
- 🔄 **Polymorphism** – Overridden `setName()`, `setEmail()`, etc. with DB-aware behavior.
- 🎭 **Abstraction** – `User` enforces subclass-specific setter implementations.
- 📦 **Encapsulation** – Private/protected fields with controlled access via getters and setters.

## 👥 Contributors

- 👤 **K R Randika**
- 👤 **A M Sineth Adhikari**
- 👤 **E K G S Kithmanthi**
- 👤 **W A J M Premarathne**
- 👤 **K G P P Koralegama**
