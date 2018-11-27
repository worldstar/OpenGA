/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.util;

import javax.activation.FileDataSource;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;
import org.simplejavamail.util.ConfigLoader;

/**
 *
 * @author user2
 */
public class SendEmail {  
  String senderName = "YOUR_NAME";
  String senderEmail = "Developer@gmail.com";
  String receiverName = "receiverName";
  String receiverEmail = "receiver@gmail.com";
  String senderPassword = "YOUR_PASSWORD";
  
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
              .withSMTPServer("smtp.gmail.com", 587, senderEmail, senderPassword)
              .withTransportStrategy(TransportStrategy.SMTP_TLS)
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
    SendEmail1.startSendEmail("Topic", "Content here.");
  }
  
}
