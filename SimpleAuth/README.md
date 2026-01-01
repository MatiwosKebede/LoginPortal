# Simple Login System

A basic Java login system with MySQL/SQLite database support.

## Setup

### Option 1: MySQL Setup
1. Install MySQL
2. Run: `mysql -u root -p < setup_mysql.sql`
3. Update database credentials in DatabaseHelper.java if needed

### Option 2: SQLite (No setup needed)
The system will automatically use SQLite if MySQL is not available.

## Running

1. Place MySQL connector JAR in `lib/` folder
2. Make run script executable: `chmod +x run.sh`
3. Run: `./run.sh`

## Features
- User registration
- Login/Logout
- Password hashing (SHA-256)
- Profile viewing
- Password changing
- List all users

## Default Test Users
- admin / admin
- user1 / 123456
- test / test123
