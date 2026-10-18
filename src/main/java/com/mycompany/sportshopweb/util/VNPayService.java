
package com.mycompany.sportshopweb.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class VNPayService {

    private VNPayService() {
    }

    public static String createPaymentUrl(
            long amountVnd,
            String txnRef,
            String orderInfo,
            String ipAddress) throws Exception {

        if (amountVnd <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be positive.");
        }

        Map<String, String> params = new TreeMap<>();

        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", VNPayConfig.getTmnCode());
        params.put("vnp_Amount", String.valueOf(amountVnd * 100));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", txnRef);
        params.put("vnp_OrderInfo", orderInfo);
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", VNPayConfig.getReturnUrl());
        params.put("vnp_IpAddr", ipAddress);
        params.put("vnp_CreateDate",
                java.time.format.DateTimeFormatter
                        .ofPattern("yyyyMMddHHmmss")
                        .withZone(java.time.ZoneId.of("Asia/Ho_Chi_Minh"))
                        .format(java.time.Instant.now()));

        String query = buildQuery(params);
        String secureHash = hmacSHA512(
                VNPayConfig.getHashSecret(),
                query);

        return VNPayConfig.PAYMENT_URL
                + "?" + query
                + "&vnp_SecureHash=" + secureHash;
    }

    private static String buildQuery(Map<String, String> params)
            throws Exception {

        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (result.length() > 0) {
                result.append('&');
            }

            result.append(URLEncoder.encode(
                    entry.getKey(), StandardCharsets.UTF_8.name()));

            result.append('=');

            result.append(URLEncoder.encode(
                    entry.getValue(), StandardCharsets.UTF_8.name()));
        }

        return result.toString();
    }

    private static String hmacSHA512(String secret, String data)
            throws Exception {

        Mac hmac = Mac.getInstance("HmacSHA512");
        SecretKeySpec key = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA512");

        hmac.init(key);

        byte[] hash = hmac.doFinal(
                data.getBytes(StandardCharsets.UTF_8));

        StringBuilder result = new StringBuilder();

        for (byte b : hash) {
            result.append(String.format("%02x", b & 0xff));
        }

        return result.toString();
    }

    public static boolean isValidSignature(
            Map<String, String> params) throws Exception {

        Map<String, String> sorted = new TreeMap<>(params);
        String receivedHash = sorted.remove("vnp_SecureHash");

        sorted.remove("vnp_SecureHashType");

        if (receivedHash == null || receivedHash.isBlank()) {
            return false;
        }

        String calculatedHash = hmacSHA512(
                VNPayConfig.getHashSecret(),
                buildQuery(sorted));

        return MessageDigest.isEqual(
                receivedHash.toLowerCase().getBytes(StandardCharsets.US_ASCII),
                calculatedHash.getBytes(StandardCharsets.US_ASCII));
    }
}
