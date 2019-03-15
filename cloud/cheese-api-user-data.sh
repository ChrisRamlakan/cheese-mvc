#!/bin/bash
# Install Java
apt-get update -y && apt-get install -y openjdk-8-jdk

# Create user
useradd -M cheese-api
mkdir /opt/cheese-api
mkdir /etc/opt/cheese-api
chown -R cheese-api:cheese-api /opt/cheese-api /etc/opt/cheese-api
chmod 777 /opt/cheese-api

# Write Cheese API config file
cat << EOF > /etc/opt/cheese-api/cheese-api.config
APP_DB_HOST=CHANGE TO YOUR RDS ENDPOINT URL (example: rds-instance.us-east-2.rds.amazonaws.com)
APP_DB_PORT=5432
APP_DB_NAME=airwaze_db
APP_DB_USER=airwaze_user
APP_DB_PASS=verysecurepassword
EOF

# Write systemd unit file
cat << EOF > /etc/systemd/system/cheese-api.service
[Unit]
Description=Cheese API
After=syslog.target

[Service]
User=cheese-api
EnvironmentFile=/etc/opt/cheese-api/cheese-api.config
ExecStart=/usr/bin/java -jar /opt/cheese-api/app.jar SuccessExitStatus=143
Restart=no

[Install]
WantedBy=multi-user.target
EOF

systemctl enable cheese-api.service

