
# Stage 1: Build WAR
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Run application
FROM tomcat:11.0-jdk17-temurin

# Configure Tomcat to listen on Render's default HTTP port
RUN sed -i 's/port="8080"/port="10000"/' \
    /usr/local/tomcat/conf/server.xml

# Deploy the application at the root URL
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /build/target/SportShopWeb-1.0-SNAPSHOT.war \
    /usr/local/tomcat/webapps/ROOT.war

EXPOSE 10000

CMD ["catalina.sh", "run"]
