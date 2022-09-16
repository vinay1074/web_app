#!/bin/bash

sudo amazon-linux-extras install java-openjdk11

sudo wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.65/bin/apache-tomcat-9.0.65.tar.gz

sudo mkdir /usr/local/tomcat9

cd /usr/local/tomcat9/

sudo tar -xvf /home/ec2-user/apache-tomcat-9.0.65.tar.gz

sudo mv apache-tomcat-9.0.65/* .

sudo useradd -r tomcat

sudo chown -R tomcat:tomcat /usr/local/tomcat9

sudo tee /etc/systemd/system/tomcat.service<<EOF
[Unit]
Description=Tomcat Server
After=syslog.target network.target

[Service]
Type=forking
User=tomcat
Group=tomcat

Environment=CATALINA_HOME=/usr/local/tomcat9
Environment=CATALINA_BASE=/usr/local/tomcat9
Environment=CATALINA_PID=/usr/local/tomcat9/temp/tomcat.pid

ExecStart=/usr/local/tomcat9/bin/catalina.sh start
ExecStop=/usr/local/tomcat9/bin/catalina.sh stop

RestartSec=12
Restart=always

[Install]
WantedBy=multi-user.target
EOF

