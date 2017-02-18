package com.ariorick.uber777.email;

import android.util.Log;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSenderClass extends javax.mail.Authenticator {
    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    private Session session;

    private Multipart _multipart;


    public MailSenderClass(String user, String password) {
        this.user = user;
        this.password = password;

        _multipart = new MimeMultipart();

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        //props.put("mail.smtp.socketFactory.fallback", "false");
        //props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(props, this);
    }

    private synchronized void addAttachments(String[] filenames) {
        for (String filename : filenames) {
            if (filename != null)
                addAttachment(filename);
        }
    }

    private synchronized void addAttachment(String filename) {
        DataSource source = new FileDataSource(filename);
        BodyPart messageBodyPart = new MimeBodyPart();
        try {
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            _multipart.addBodyPart(messageBodyPart);
        } catch (Exception e) {
            Log.e("MailSenderClass", e.getMessage());
        }

    }


    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String subject, String body, String sender, String recipients, String[] filenames) {
        try {
            final MimeMessage message = new MimeMessage(session);

            // кто
            message.setSender(new InternetAddress(sender));
            // о чём
            message.setSubject(subject);
            // кому
            if (recipients.indexOf(',') > 0)
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(recipients));
            else
                message.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(recipients));

            // хочет сказать
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            _multipart.addBodyPart(messageBodyPart);

            // и что показать
            if (filenames != null) {
                addAttachments(filenames);
            }

            message.setContent(_multipart);
            Transport.send(message);
        } catch (Exception e) {
            Log.e("sendMail", e.toString());
        }
    }
}
