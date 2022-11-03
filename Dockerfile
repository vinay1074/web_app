FROM tomcat:8.5.35-jre10
COPY ./target/web_app.war /usr/local/tomcat/webapps/
EXPOSE 8080
RUN chmod +x /usr/local/tomcat/bin/catalina.sh
CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]