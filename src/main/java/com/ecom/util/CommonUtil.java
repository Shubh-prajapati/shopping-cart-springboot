package com.ecom.util;

import com.ecom.model.ProductOrder;
import com.ecom.model.UserDtls;
import com.ecom.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Optional;

@Component
public class CommonUtil {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    /**
     * Sends password reset email
     */
    public Boolean sendMail(String url, String recipientEmail)
            throws MessagingException, UnsupportedEncodingException {

        if (recipientEmail == null || recipientEmail.isBlank()) {
            System.out.println("⚠️ Skipping password reset mail: recipient email empty");
            return false;
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("shubhpraj7028@gmail.com", "Shopping Cart");
        helper.setTo(recipientEmail);

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + url + "\">Change my password</a></p>";

        helper.setSubject("Password Reset");
        helper.setText(content, true);

        mailSender.send(message);
        return true;
    }

    /**
     * Generates absolute base URL from request
     */
    public static String generateUrl(HttpServletRequest request) {
        String siteUrl = request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(), "");
    }

    /**
     * Sends confirmation / status update email for a product order
     */
    public Boolean sendMailForProductOrder(ProductOrder order, String status) throws Exception {

        String email = Optional.ofNullable(order.getOrderAddress())
                .map(addr -> addr.getEmail())
                .orElse("");

        // ✅ Prevent IllegalAddressException
        if (email == null || email.isBlank()) {
            System.out.println("⚠️ Skipping order mail: empty or invalid recipient email");
            return false;
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("shubhpraj7028@gmail.com", "Shopping Cart");
        helper.setTo(email);

        String msg = """
            <p>Hello [[name]],</p>
            <p>Your order status has been updated.</p>
            <p><strong>Product:</strong> [[productName]]</p>
            <p><strong>Category:</strong> [[category]]</p>
            <p><strong>Quantity:</strong> [[quantity]]</p>
            <p><strong>Price:</strong> ₹[[price]]</p>
            <p><strong>Payment Type:</strong> [[paymentType]]</p>
            <p><strong>Status:</strong> [[orderStatus]]</p>
            <p>Thank you for shopping with us!</p>
        """;

        // ✅ Safe replacements with fallback defaults
        msg = msg.replace("[[name]]",
                Optional.ofNullable(order.getOrderAddress().getFirstName()).orElse("Customer"));
        msg = msg.replace("[[orderStatus]]", Optional.ofNullable(status).orElse("Unknown"));
        msg = msg.replace("[[productName]]",
                Optional.ofNullable(order.getProduct()).map(p -> p.getTitle()).orElse("N/A"));
        msg = msg.replace("[[category]]",
                Optional.ofNullable(order.getProduct()).map(p -> p.getCategory()).orElse("N/A"));
        msg = msg.replace("[[quantity]]", String.valueOf(order.getQuantity()));
        msg = msg.replace("[[price]]", String.valueOf(order.getPrice()));
        msg = msg.replace("[[paymentType]]", Optional.ofNullable(order.getPaymentType()).orElse("N/A"));

        helper.setSubject("Your Order Status Update");
        helper.setText(msg, true); // send as HTML

        mailSender.send(message);
        return true;
    }

    /**
     * Get currently logged-in user details
     */
    public UserDtls getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        return userService.getUserByEmail(email);
    }
}