/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ac31007_group_8.quiz.util;


import com.sun.mail.smtp.SMTPTransport;
import java.io.File;
import java.security.Security;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


/**
 *
 * @author Vlad
 * ADOPTED FROM http://stackoverflow.com/questions/3649014/send-email-using-java
 * if you are using avast, you should remove Secure Port: 465 in redirect settings
 */
public class GoogleMail {
    private GoogleMail() {
    }

    
    
    // AddressException if the email address parse failed
    //MessagingException if the connection is dead or not in the connected state or if the message is not a MimeMessage
    public static void Send(final String username, final String password, String recipientEmail, String title, String message, File f) throws AddressException, MessagingException {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtps.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtps.auth", "true");

        props.put("mail.smtps.quitwait", "false");

        Session session = Session.getInstance(props, null);

      
        final MimeMessage msg = new MimeMessage(session);

       
        msg.setFrom(new InternetAddress(username));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));

        msg.setSubject(title);
        msg.setSentDate(new Date());

        
        Multipart multipart = new MimeMultipart("mixed");

      
        //email text
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(message, "text/html; charset=utf-8");
        
        //email attachment
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        String file = f.getAbsolutePath();
        DataSource source = new FileDataSource(file);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName("Results.html");
        
        
        multipart.addBodyPart(messageBodyPart);
        multipart.addBodyPart(textPart);
        msg.setContent(multipart);


        
        SMTPTransport t = (SMTPTransport)session.getTransport("smtps");

        t.connect("smtp.gmail.com", username, password);
        t.sendMessage(msg, msg.getAllRecipients());      
        t.close();
    }
}
