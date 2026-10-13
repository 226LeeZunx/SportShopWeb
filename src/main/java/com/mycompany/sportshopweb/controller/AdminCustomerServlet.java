
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.CustomerDAO;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.util.DBUtil;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminCustomerServlet", urlPatterns = { "/admin/customers" })
public class AdminCustomerServlet extends HttpServlet {

    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");

        if (keyword != null) {
            keyword = keyword.trim();
        }

        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            // Lấy khách hàng cùng User và MembershipTier
            // để JSP có thể hiển thị thông tin sau khi đóng EntityManager.
            List<Customer> customers = em.createQuery(
                    "SELECT c FROM Customer c " +
                            "JOIN FETCH c.user " +
                            "LEFT JOIN FETCH c.membershipTier " +
                            "ORDER BY c.customerId DESC",
                    Customer.class).getResultList();

            // Lọc theo tên, email hoặc số điện thoại.
            if (keyword != null && !keyword.isEmpty()) {
                final String search = keyword.toLowerCase();

                customers = customers.stream()
                        .filter(customer -> {
                            String firstName = customer.getFirstName();

                            String lastName = customer.getLastName();

                            String fullName = ((firstName == null ? "" : firstName)
                                    + " "
                                    + (lastName == null ? "" : lastName))
                                    .toLowerCase();

                            String email = customer.getUser().getEmail();

                            String phone = customer.getUser().getPhone();

                            email = email == null
                                    ? ""
                                    : email.toLowerCase();

                            phone = phone == null
                                    ? ""
                                    : phone.toLowerCase();

                            return fullName.contains(search)
                                    || email.contains(search)
                                    || phone.contains(search);
                        })
                        .toList();
            }

            // Thống kê khách hàng.
            long totalCustomers = customers.size();
            long newCustomers = customers.stream()
                    .filter(customer -> customer.getUser().getCreatedAt() != null)
                    .count();

            // Lấy ID các khách hàng có đơn hàng.
            List<Integer> customerIdsWithOrders = em.createQuery(
                    "SELECT DISTINCT o.customer.customerId " +
                            "FROM Order o",
                    Integer.class).getResultList();

            long customersWithOrders = customers.stream()
                    .filter(customer -> customerIdsWithOrders.contains(
                            customer.getCustomerId()))
                    .count();

            long customersWithoutOrders = totalCustomers - customersWithOrders;

            request.setAttribute("customers", customers);
            request.setAttribute("totalCustomers", totalCustomers);
            request.setAttribute("newCustomers", newCustomers);
            request.setAttribute(
                    "customersWithOrders",
                    customersWithOrders);
            request.setAttribute(
                    "customersWithoutOrders",
                    customersWithoutOrders);
            request.setAttribute("keyword", keyword);

            // Nếu URL có ?id=..., lấy khách hàng được chọn.
            String idParam = request.getParameter("id");

            if (idParam != null && !idParam.isBlank()) {
                try {
                    int customerId = Integer.parseInt(idParam);

                    Customer selectedCustomer = customerDAO.findById(customerId);

                    request.setAttribute(
                            "selectedCustomer",
                            selectedCustomer);

                    if (selectedCustomer != null) {
                        List<Order> customerOrders = em.createQuery(
                                "SELECT DISTINCT o FROM Order o " +
                                        "LEFT JOIN FETCH o.orderItems " +
                                        "WHERE o.customer.customerId = :id " +
                                        "ORDER BY o.orderId DESC",
                                Order.class)
                                .setParameter("id", customerId)
                                .getResultList();

                        request.setAttribute(
                                "customerOrders",
                                customerOrders);
                    } else {
                        request.setAttribute(
                                "customerOrders",
                                List.of());
                    }

                } catch (NumberFormatException e) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid customer ID.");
                    return;
                }
            }

        } finally {
            em.close();
        }

        request.getRequestDispatcher(
                "/views/admin/customers.jsp").forward(request, response);
    }
}