#!/bin/bash

# Create user, folder
useradd -M cheese-spa
mkdir /home/ubuntu/cheese-spa
# Allow the react app to be sshd to here
chmod 777 /home/ubuntu/cheese-spa

# TODO: could download the build.tgz from a public available s3 bucket
# TODO: unzip the app to /home/ubuntu/cheese-spa (will require changing permissions of build.tgz after download)

# Write sh file that will start web server in correct folder
# For this to work, you will need to paste the SPA build folder to /home/ubuntu/cheese-spa
cat << EOF > /home/ubuntu/cheese-spa/run-server.sh
#!/bin/bash
cd /home/ubuntu/cheese-spa/build
pwd
python3 -m http.server 5000
EOF

chown cheese-spa:cheese-spa /home/ubuntu/cheese-spa/run-server.sh
chmod 544 /home/ubuntu/cheese-spa/run-server.sh

# Write systemd config file
cat << EOF > /etc/systemd/system/cheese-spa.service
[Unit]
Description=Serve Cheese SPA
After=syslog.target

[Service]
User=cheese-spa
ExecStart=/home/ubuntu/cheese-spa/run-server.sh SuccessExitStatus=143
Restart=no

[Install]
WantedBy=multi-user.target
EOF


# TODO: systemctl enable cheese-spa.service
# TODO: systemctl start cheese-spa.service
