package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Brand;
import java.util.List;
public class BrandDAOTest {

    public void testGetAllBrand() {
        BrandDAO brandDAO = new BrandDAO();
        List<Brand> brands = brandDAO.getAllBrand();
        
        // 1. Kiểm tra xem danh sách trả về có khác null hay không
        
        // 2. In ra số lượng và thông tin chi tiết để quan sát trên console
        System.out.println("=== KẾT QUẢ TEST BRAND DAO ===");
        System.out.println("Số lượng brand tìm thấy: " + brands.size());
        System.out.println("=============================");
    }
}