
# Stage 1: Build Java WAR
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /build
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Run Java + Python
FROM tomcat:11.0-jdk17-temurin

RUN apt-get update \
    && apt-get install -y --no-install-recommends python3 python3-pip \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy Python application
COPY . .

# Install Python dependencies, including FastAPI and Uvicorn
RUN pip3 install --no-cache-dir --break-system-packages -r python-rag/requirements.txt

# Configure Tomcat for Render
RUN sed -i 's/port="8080"/port="10000"/' \
    /usr/local/tomcat/conf/server.xml

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /build/target/SportShopWeb-1.0-SNAPSHOT.war \
    /usr/local/tomcat/webapps/ROOT.war

COPY start.sh /start.sh
RUN sed -i 's/\r$//' /start.sh
CMD ["/bin/bash", "/start.sh"]

EXPOSE 10000

