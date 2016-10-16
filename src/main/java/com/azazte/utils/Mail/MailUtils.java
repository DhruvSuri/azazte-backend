package com.azazte.utils.Mail;

import jersey.repackaged.com.google.common.base.Joiner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

/**
 * Created by home on 20/08/16.
 */
public class MailUtils {
    public static final String FROM = "mailbot@finup.in";

    public static void sendMail(String subject, String text, List<String> mailerList) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "localhost");
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(FROM));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(Joiner.on(",").join(mailerList)));
            msg.setSubject(subject);
            msg.setText(text);
            Transport.send(msg);
        } catch (MessagingException e) {
            // ...
        }
    }
}
