/*
 * Reference1: http://www.simplejavamail.org/#/features
 * Reference2: http://rx1226.pixnet.net/blog/post/343676180-%5Bjava%5D-3-9-javamail---%E4%BD%BF%E7%94%A8gmail
 */
package openga.util;

import javax.activation.FileDataSource;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;

/**
 *
 * @author Shih-Hsin Chen
 */
public class SendEmail {  
  String senderName = "YOUR_NAME";
  String senderAccount = "YourAccount";//Don't add "@gmail.com"
  String senderEmail = senderAccount+"@gmail.com";
  String receiverName = "Receiver_Name";
  String receiverEmail = "SecondAccount@gmail.com";
  String senderPassword = "PASSWORD";
  
  public void startSendEmail(String emailSubject, String msg){
    Email email = getEmailObject(emailSubject, msg);
    Mailer mailer = getMailer();
    mailer.sendMail(email);  
  }  
  
  public void startSendEmail(String emailSubject, String msg, String fileName){
    Email email = getEmailObject(emailSubject, msg, fileName);
    Mailer mailer = getMailer();
    mailer.sendMail(email);  
  }
  
  public Email getEmailObject(String emailSubject, String msg){
    Email email = EmailBuilder.startingBlank()
        .from(senderName, senderEmail)
        .to(receiverName, receiverEmail)
        .withSubject(emailSubject)
        .withPlainText(msg)    
        .buildEmail();
    
    return email;
  }  
  
  public Email getEmailObject(String emailSubject, String msg, String fileName){
    Email email = EmailBuilder.startingBlank()
        .from(senderName, senderEmail)
        .to(receiverName, receiverEmail)
        .withSubject(emailSubject)
        .withPlainText(msg)
        .withAttachment(""+fileName, new FileDataSource(fileName))    
        .buildEmail();
    
    return email;
  }
  
  public Mailer getMailer(){
    Mailer mailer = MailerBuilder
              .withSMTPServer("smtp.gmail.com", 465, senderAccount, senderPassword)
              .withTransportStrategy(TransportStrategy.SMTPS)
//              .withProxy("socksproxy.host.com", 1080, "proxy user", "proxy password")
              .withSessionTimeout(10 * 1000)
              .clearEmailAddressCriteria() // turns off email validation
//              .withProperty("mail.smtp.sendpartial", true)
              .withDebugLogging(true)
              .buildMailer();  
    
    return mailer;
  }
  
  public static void main(String[] args) {
    SendEmail SendEmail1 = new SendEmail();
//    SendEmail1.startSendEmail("Topic", "Content here.");
    SendEmail1.startSendEmail("Topic", "Content here.", "README.md");
  }
  
}
