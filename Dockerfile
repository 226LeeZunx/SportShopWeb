# ==============================================================================
# DOCKERFILE CHO MODULE PYTHON-RAG (SPORTSHOP AI ASSISTANT)
# Base Image: Python 3.11 Slim (nhẹ, bảo mật và tối ưu dung lượng)
# Build Context: Thư mục gốc dự án (SportShopWeb)
# ==============================================================================

FROM python:3.11-slim

# 1. Thiết lập các biến môi trường hệ thống cho Python
ENV PYTHONDONTWRITEBYTECODE=1 \
    PYTHONUNBUFFERED=1 \
    PYTHONIOENCODING=utf-8 \
    LANG=C.UTF-8 \
    LC_ALL=C.UTF-8

# 2. Đặt thư mục làm việc bên trong container
WORKDIR /app

# 3. Cài đặt các package hệ thống cần thiết
RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    && rm -rf /var/lib/apt/lists/*

# 4. Copy file requirements.txt từ thư mục python-rag vào container để tận dụng Docker Cache
COPY python-rag/requirements.txt ./

# 5. Cài đặt các thư viện Python (OpenAI, PyMongo, Pydantic, Requests, NumPy, python-dotenv)
RUN pip install --no-cache-dir --upgrade pip && \
    pip install --no-cache-dir -r requirements.txt

# 6. Copy toàn bộ mã nguồn của module python-rag vào thư mục làm việc /app
COPY python-rag/ .

# 7. Mặc định chạy script query_rag.py để tiếp nhận câu hỏi
ENTRYPOINT ["python", "query_rag.py"]

# Tham số mặc định khi không truyền câu hỏi
CMD ["Xin chào, bạn có thể tư vấn sản phẩm thể thao nào tốt?"]
