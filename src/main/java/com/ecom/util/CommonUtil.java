package com.ecom.util;

import com.ecom.model.ProductOrder;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Component
public class CommonUtil {

    @Autowired
    private JavaMailSender mailSender;
    public Boolean sendMail(String url, String reciepentEmail) throws MessagingException, UnsupportedEncodingException {
       MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);

        helper.setFrom("shubhpraj7028@gmail.com", "Shopping Cart");
        helper.setTo(reciepentEmail);


        String content="<p>Hello, </p>"
                +"<p>You Have requested to resent your password.</p>"
                +"<p>Click link in below to Change your password</p>"
                +"<p><a href=\""+ url+ "\">Change my password</a></p>";
        helper.setSubject("Password reset");
        helper.setText(content,true);

        mailSender.send(message);
        return true;
    }

    public static String generateUrl(HttpServletRequest request) {


        String siteUrl = request.getRequestURL().toString();

        return siteUrl.replace( request.getServletPath(),"");

    }

    String msg=null;

    public Boolean sendMailForProductOrder(ProductOrder order, String status) throws Exception {

        msg="<p>Hello [[name]]</p>"

                +"<p>Thank Your Order <b>[[orderStatus]]</b>.</p>"
                +"<p> <b>Product Details:</b> </p>"
                +"<p> Name:[[productName]] </p>"
                +"<p> Category:[[category]] </p>"
                +"<p> Quantity:[[quantity]] </p>"
                +"<p> Price:[[price]]</p>"
                +"<p> Payment Type:[[paymentType]] </p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("shubhpraj7028@gmail.com", "Shopping Cart");
        helper.setTo(order.getOrderAddress().getEmail());

        // ✅ Define your email template here
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

        // ✅ Safe replacements with default fallback (to prevent NullPointerException)
        msg = msg.replace("[[name]]", Optional.ofNullable(order.getOrderAddress().getFirstName()).orElse("Customer"));
        msg = msg.replace("[[orderStatus]]", Optional.ofNullable(status).orElse("Unknown"));
        msg = msg.replace("[[productName]]", Optional.ofNullable(order.getProduct().getTitle()).orElse("N/A"));
        msg = msg.replace("[[category]]", Optional.ofNullable(order.getProduct().getCategory()).orElse("N/A"));
        msg = msg.replace("[[quantity]]", String.valueOf(order.getQuantity()));
        msg = msg.replace("[[price]]", String.valueOf(order.getPrice()));
        msg = msg.replace("[[paymentType]]", Optional.ofNullable(order.getPaymentType()).orElse("N/A"));


        helper.setSubject("Your Order Status Update");
        helper.setText(msg, true); // true = send as HTML

        mailSender.send(message);
        return true;
    }

}
