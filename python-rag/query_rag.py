import sys
import os
import io
from dotenv import load_dotenv

# Ép buộc luồng chuẩn stdout sử dụng mã hóa UTF-8 để khắc phục lỗi tiếng Việt trên Windows/Java
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

# Nạp biến môi trường từ file .env cùng thư mục
dotenv_path = os.path.join(os.path.dirname(__file__), '.env')
load_dotenv(dotenv_path=dotenv_path)

from rag_service import RAGPipeline

def answer_user_question(question: str, top_k: int = 3):
    """
    Hàm nhận câu hỏi, kết nối MongoDB Atlas lấy RAG chunks và trả lời qua LLM.
    """
    try:
        rag = RAGPipeline()
        # 1. Truy vấn các chunk tương đồng nhất từ MongoDB Atlas và sinh câu trả lời
        answer = rag.query(question, top_k=top_k)
        return answer
    except Exception as e:
        return f"Lỗi xử lý RAG: {str(e)}"

if __name__ == "__main__":
    # Nhận câu hỏi từ tham số dòng lệnh (được truyền từ Java ProcessBuilder)
    if len(sys.argv) > 1:
        user_query = sys.argv[1]
        response = answer_user_question(user_query, top_k=3)
        print(response)
    else:
        # Trường hợp chạy test trực tiếp không có tham số
        test_query = "Áo thun Adidas Climacool có giá bao nhiêu?"
        print(f"❓ Câu hỏi test: {test_query}")
        response = answer_user_question(test_query, top_k=2)
        print("🤖 Trả lời:")
        print(response)