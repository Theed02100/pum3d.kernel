FROM tomcat:9-jdk11

# Copy .war
COPY target/ROOT.war /usr/local/tomcat/webapps/

# Start app
CMD ["catalina.sh","run"]
EXPOSE 8080