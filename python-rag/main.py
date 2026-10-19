import sys
import os
from dotenv import load_dotenv

# Tự động nạp file .env nằm cùng thư mục
dotenv_path = os.path.join(os.path.dirname(__file__), '.env')
load_dotenv(dotenv_path=dotenv_path)

from models import Document
from rag_service import RAGPipeline

def main():
    # Khởi tạo RAG Pipeline (tự động kết nối MongoDB Atlas, Jina v5 & Custom LLM)
    try:
        rag = RAGPipeline()
    except Exception as e:
        print(f"Lỗi khởi tạo RAGPipeline: {str(e)}")
        return

    # ---------------------------------------------------------
    # TRƯỜNG HỢP 1: Được gọi từ Java Servlet (qua ProcessBuilder)
    # Ví dụ lệnh gọi: python main.py "câu hỏi của user"
    # ---------------------------------------------------------
    if len(sys.argv) > 1:
        query_text = sys.argv[1]
        try:
            answer = rag.query(query_text, top_k=3)
            # In ra kết quả để Java đọc từ luồng chuẩn (stdout)
            print(answer)
        except Exception as e:
            print(f"Lỗi xử lý RAG từ Python: {str(e)}")
        return

    # ---------------------------------------------------------
    # TRƯỜNG HỢP 2: Chạy kiểm thử trực tiếp tại Terminal (Test mode)
    # ---------------------------------------------------------
    print("==========================================")
    print("   BẮT ĐẦU KIỂM THỬ HỆ THỐNG RAG (PYTHON)  ")
    print("==========================================")

    # 1. Tạo tài liệu mẫu về sản phẩm thể thao
    sample_text = """
    Giày chạy bộ Nike Air Zoom Pegasus 40 có thiết kế thoáng khí, đệm giày phản lực tốt, phù hợp cho vận động viên chạy cự ly dài. Giá bán niêm yết là 3.200.000 VNĐ. Bảo hành chính hãng 12 tháng.
    Áo thun thể thao Adidas Climacool thấm hút mồ hôi cực tốt, công nghệ làm mát nhanh chóng. Giá bán 650.000 VNĐ.
    Bóng đá chính hãng Molten F5U5000 đạt chuẩn FIFA Quality Pro, vỏ làm bằng da tổng hợp cao cấp chịu lực tốt. Giá bán 1.800.000 VNĐ.
    Vợt cầu lông Yonex Astrox 99 Pro thiết kế chuyên công, trợ lực tốt cho người chơi phong trào nâng cao và chuyên nghiệp. Giá bán 3.800.000 VNĐ.
    """
    
    doc = Document(
        doc_id="sport_products_catalog_01",
        content=sample_text,
        metadata={"category": "products", "store": "SportShop"}
    )

    # 2. Ingest (Nạp và Vector hóa) tài liệu lên MongoDB Atlas
    print("\n📥 Đang thực hiện Ingest tài liệu vào MongoDB Atlas...")
    try:
        rag.ingest(doc)
    except Exception as e:
        print(f"Lỗi khi Ingest tài liệu: {str(e)}")
        return

    # 3. Thử nghiệm truy vấn
    test_query = "Giày Nike Air Zoom Pegasus 40 giá bao nhiêu và bảo hành mấy tháng?"
    print(f"\n❓ Câu hỏi kiểm thử: '{test_query}'")
    
    print("\n🔄 Đang tìm kiếm và sinh câu trả lời từ AI...")
    try:
        answer = rag.query(test_query, top_k=2)
        print("\n------------------------------------------")
        print("🤖 KẾT QUẢ TRẢ LỜI TỪ RAG:")
        print(answer)
        print("==========================================")
    except Exception as e:
        print(f"Lỗi khi truy vấn RAG: {str(e)}")

if __name__ == "__main__":
    main()