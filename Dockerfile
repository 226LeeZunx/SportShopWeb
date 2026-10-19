# ==========================================
# GIAI ĐOẠN 1: Build Java Maven Project thành file WAR
# ==========================================
FROM maven:3.9-eclipse-temurin-17 AS maven_builder
WORKDIR /app
COPY . .
# Build file WAR (bỏ qua test để tăng tốc độ)
RUN mvn clean package -DskipTests

# ==========================================
# GIAI ĐOẠN 2: Runtime Image (Tomcat 10.1 + Java 17 + Python 3)
# ==========================================
FROM tomcat:10.1-jdk17

# Thiết lập múi giờ Việt Nam (UTC+7 / Asia/Ho_Chi_Minh) cho container và máy ảo Java
ENV TZ=Asia/Ho_Chi_Minh \
    JAVA_OPTS="-Duser.timezone=Asia/Ho_Chi_Minh"

# Cài đặt Python 3, pip và công cụ tạo môi trường ảo venv trên nền Linux
RUN apt-get update && apt-get install -y \
    python3 \
    python3-pip \
    python3-venv \
    && rm -rf /var/lib/apt/lists/*

# Thiết lập thư mục làm việc cho phần Python
WORKDIR /opt/python-rag

# Copy file requirements.txt chính xác từ thư mục python-rag/ lên container
COPY python-rag/requirements.txt .

# Tạo môi trường ảo .venv, nâng cấp pip và cài đặt các thư viện Python
RUN python3 -m venv .venv && \
    .venv/bin/pip install --no-cache-dir --upgrade pip && \
    .venv/bin/pip install --no-cache-dir -r requirements.txt

# Copy toàn bộ mã nguồn Python còn lại trong thư mục python-rag/ vào container
COPY python-rag/ .

# Xóa ứng dụng mặc định của Tomcat và đưa file WAR của Java vào làm ứng dụng gốc (ROOT)
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=maven_builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Vô hiệu hóa cổng shutdown 8005 và cấu hình Tomcat lắng nghe trên 0.0.0.0 theo biến môi trường PORT của Render
ENV PORT=8080
RUN echo 'export JAVA_OPTS="$JAVA_OPTS -Duser.timezone=Asia/Ho_Chi_Minh -DPORT=${PORT:-8080}"' > /usr/local/tomcat/bin/setenv.sh && \
    chmod +x /usr/local/tomcat/bin/setenv.sh && \
    sed -i 's/port="8005"/port="-1"/' /usr/local/tomcat/conf/server.xml && \
    sed -i 's/port="8080"/port="${PORT}" address="0.0.0.0"/' /usr/local/tomcat/conf/server.xml

# Khởi chạy Tomcat
CMD ["catalina.sh", "run"]
