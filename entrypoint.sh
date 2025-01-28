#!/bin/bash
set -e

echo "Starting MySQL service..."
service mysql start

echo "Waiting for MySQL to fully start..."
while ! nc -z 127.0.0.1 3306; do
  sleep 1
done

echo "Configuring root user to use mysql_native_password with password 'root'..."
mysql -u root <<EOF
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root';
FLUSH PRIVILEGES;
EOF

echo "Creating database cibel_db (if not exists) and importing data..."
mysql -u root -proot -e "CREATE DATABASE IF NOT EXISTS cibel_db;"
mysql -u root -proot cibel_db < /app/backup_cibel_db.sql

echo "Starting Java application..."
java -jar /app/CIBELService.jar
