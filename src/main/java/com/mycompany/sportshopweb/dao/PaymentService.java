
package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.dao.PaymentDAO;
import com.mycompany.sportshopweb.model.Payment;

import java.util.List;

//Service xử lý nghiệp vụ liên quan đến Payment.

public class PaymentService {

    // DAO chịu trách nhiệm tương tác với database.
    private final PaymentDAO paymentDAO;

    // Constructor
    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
    }

    /**
     * Tìm một Payment theo paymentId.
     * 
     * @param paymentId ID của bản ghi thanh toán
     * @return Payment nếu tìm thấy, ngược lại trả về null
     */
    public Payment findById(int paymentId) {
        return paymentDAO.findById(paymentId);
    }

    /**
     * Lấy danh sách Payment thuộc một đơn hàng.
     * 
     * @param orderId
     * @return Danh sách Payment; danh sách rỗng nếu không có kết quả
     */
    public List<Payment> findByOrderId(int orderId) {
        return paymentDAO.findByOrderId(orderId);
    }

    /**
     * Lấy toàn bộ danh sách Payment.
     * 
     * @return Danh sách Payment
     */
    public List<Payment> findAll() {
        return paymentDAO.findAll();
    }

    /**
     * Thêm một bản ghi Payment mới.
     * 
     * @param payment đối tượng Payment cần lưu
     */
    public void insertPayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException(
                    "Payment must not be null.");
        }

        paymentDAO.insertPayment(payment);
    }

    /**
     * Cập nhật một bản ghi Payment đã tồn tại.
     * 
     * @param payment đối tượng Payment cần cập nhật
     */
    public void updatePayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException(
                    "Payment must not be null.");
        }

        paymentDAO.updatePayment(payment);
    }
}
