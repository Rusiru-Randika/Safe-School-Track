
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import io.github.cdimascio.dotenv.Dotenv;


import java.util.Properties;

class NotificationManager {
  public static void sendMail(String recipient, String subject, String body) throws MessagingException {
      System.out.println("preparing to send email");
      Properties props = new Properties();
      props.put("mail.smtp.host", "smtp.gmail.com");
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.port", "587");

      Dotenv dotenv = Dotenv.configure().filename("Credential.env").load();

      String ourEmail = dotenv.get("EMAIL_USERNAME");
      String ourPassword = dotenv.get("EMAIL_PASSWORD");

      System.out.println(ourEmail);

      Session session = Session.getInstance(props, new Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
              return new PasswordAuthentication(ourEmail, ourPassword);
          }
      });
    Message message = prepareMessage(session , ourEmail, recipient , subject,body );
    Transport.send(message);
      System.out.println("\ne-mail sent successfully\n");
      System.out.println("\ne-mail is sent to " +"\""+ recipient +"\""+ " with subject " + "\""+subject+"\"\n");

  }
private static Message prepareMessage(Session session,String myEmail , String recipient, String subject, String body) {
    try {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(myEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);
        message.setContent(body, "text/html");
        return message; // Return the created message
    } catch (MessagingException e) {
        e.printStackTrace();
        return null;
    }
}
}
