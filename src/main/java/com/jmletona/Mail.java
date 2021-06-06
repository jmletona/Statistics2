package com.jmletona;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;

public class Mail {

    private final Properties properties;
    private final Session session;

    public Mail(String ruta) throws IOException {
        this.properties = new Properties();
        loadConfig(ruta);
        session = Session.getDefaultInstance(properties);
    }

    private void loadConfig(String path) throws InvalidParameterException, IOException {
        InputStream is = new FileInputStream(path);
        this.properties.load(is);
        checkConfig();
    }

    private void checkConfig() throws InvalidParameterException {

        String[] keys = {
                "mail.smtp.host",
                "mail.smtp.port",
                "mail.smtp.user",
                "mail.smtp.password",
                "mail.smtp.starttls.enable",
                "mail.smtp.auth"
        };

        for (String key : keys) {
            if (this.properties.get(key) == null) {
                throw new InvalidParameterException("password doesn't exist. " + key);
            }
        }
    }

    public void sendEmail(String title, ArrayList<String> alReport, String email, String txtFile, String pdfFile, String filePath) throws MessagingException {

        try {
            MimeMessage container = new MimeMessage(session);
            container.setFrom(new InternetAddress((String) this.properties.get("mail.smtp.user")));
            container.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            container.setSubject(title);

            Multipart multipart2 = new MimeMultipart();

            //adding body text
            MimeBodyPart messageBodyPart1 = new MimeBodyPart();
            StringBuilder bodyText = arrayListToLines(alReport);
            messageBodyPart1.setText(bodyText.toString());
            multipart2.addBodyPart(messageBodyPart1);

            //attachments

            BodyPart messageBodyPart2 = new MimeBodyPart();
            DataSource source2 = new FileDataSource(filePath+txtFile);
            messageBodyPart2.setDataHandler(new DataHandler(source2));
            messageBodyPart2.setFileName(txtFile);
            multipart2.addBodyPart(messageBodyPart2);

            BodyPart messageBodyPart3 = new MimeBodyPart();
            DataSource source3 = new FileDataSource(filePath+pdfFile);
            messageBodyPart3.setDataHandler(new DataHandler(source3));
            messageBodyPart3.setFileName(pdfFile);
            multipart2.addBodyPart(messageBodyPart3);//add second attachment to the same multipart like the first attachment

            container.setContent(multipart2); //add attachments and message to Mail

            Transport t = session.getTransport("smtp");
            t.connect((String) this.properties.get("mail.smtp.user"), (String) this.properties.get("mail.smtp.password"));
            t.sendMessage(container, container.getAllRecipients());
        }catch (MessagingException e){
            e.printStackTrace();
        }

    }

    private StringBuilder arrayListToLines(ArrayList<String> alReport) {
        StringBuilder bodyText = new StringBuilder();
        for(String line:alReport){
            bodyText.append(line).append("\n");
        }
        return bodyText;
    }
}