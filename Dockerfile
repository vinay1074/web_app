FROM tomcat:8-jre8
RUN mv /usr/local/tomcat/webapps.dist/* /usr/local/tomcat/webapps/
COPY target/web_app.war /usr/local/tomcat/webapps/