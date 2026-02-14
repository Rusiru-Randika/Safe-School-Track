import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Properties;

class NotificationManager {

    public static void sendMail(String recipient, String subject, String body) throws MessagingException {
        System.out.println("Preparing to send email...");

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");

        Dotenv dotenv = Dotenv.configure().filename("Credential.env").load();
        String ourEmail = dotenv.get("EMAIL_USERNAME");
        String ourPassword = dotenv.get("EMAIL_PASSWORD");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ourEmail, ourPassword);
            }
        });

        Message message = prepareMessage(session, ourEmail, recipient, subject, body);
        if (message != null) {
            Transport.send(message);
            System.out.println("\nEmail sent successfully to \"" + recipient + "\" with subject \"" + subject + "\"\n");
        } else {
            System.out.println("Failed to prepare email message.");
        }
    }

    private static Message prepareMessage(Session session, String from, String recipient,
                                          String subject, String body) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setContent(body, "text/html");
            return message;
        } catch (MessagingException e) {
            System.out.println("Error preparing email: " + e.getMessage());
            return null;
        }
    }
}
