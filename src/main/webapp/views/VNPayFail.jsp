<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="vi">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thanh toán thất bại</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                text-align: center;
                margin: 80px 20px;
            }

            .status {
                color: #dc3545;
                font-size: 24px;
                font-weight: bold;
            }
        </style>
    </head>

    <body>
        <h1 class="status">Thanh toán thất bại!</h1>
        <p>Giao dịch chưa được xác nhận thành công.</p>
        <a href="${pageContext.request.contextPath}/">Quay về trang chủ</a>
    </body>

    </html>