# --- Giai đoạn 1: Build file WAR bằng Maven ---
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# --- Giai đoạn 2: Chạy ứng dụng trên Tomcat ---
FROM tomcat:10.1-jdk17-chocolatey

# Cài đặt Python 3 và venv
RUN apt-get update && apt-get install -y \
    python3 \
    python3-pip \
    python3-venv \
    && rm -rf /var/lib/apt/lists/*

# Dọn dẹp ứng dụng mặc định của Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy file WAR đã build vào thư mục webapps và đổi tên thành ROOT.war
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Copy thư mục python-rag vào đúng vị trí Tomcat
COPY --from=build /app/python-rag /usr/local/tomcat/python-rag

# Khởi tạo môi trường ảo Python và cài đặt các thư viện trong requirements.txt
WORKDIR /usr/local/tomcat/python-rag
RUN python3 -m venv .venv
RUN .venv/bin/pip install --no-cache-dir -r requirements.txt

# --- QUAN TRỌNG: Vô hiệu hóa cổng shutdown 8005 của Tomcat để tránh xung đột health check với Render ---
RUN sed -i 's/port="8005" shutdown="SHUTDOWN"/port="-1" shutdown="SHUTDOWN"/' /usr/local/tomcat/conf/server.xml

# Đặt lại thư mục làm việc về Tomcat và chạy ứng dụng
WORKDIR /usr/local/tomcat
EXPOSE 8080
CMD ["catalina.sh", "run"]
