package com.example.redditClone.service;

import com.example.redditClone.exception.ActivationException;
import com.example.redditClone.models.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor

public class MailService {

    private JavaMailSender javaMailSender;
    private MailBuilder mailBuilder;

    @Async
    public void sendEmail(NotificationEmail notificationEmail) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setFrom("activation@redditclone.com");
        messageHelper.setTo(notificationEmail.getRecepient());
        messageHelper.setSubject(notificationEmail.getSubject());
        messageHelper.setText(mailBuilder.build(notificationEmail.getBody()));

        try {
            javaMailSender.send(mimeMessage);
            System.out.println("Activation Email Sent");
        } catch (MailException e) {
            throw new ActivationException("Error sending activation email to " + notificationEmail.getRecepient());
        }
    }


}



//@Service
//@AllArgsConstructor
//@Slf4j
//public class MailService {
//
//    private final JavaMailSender mailSender;
//    private final MailBuilder mailBuilder;
//
//    @Async
//    void sendEmail(NotificationEmail notificationEmail) {
//        MimeMessagePreparator messagePreparator = mimeMessage -> {
//            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
//            messageHelper.setFrom("springreddit@email.com");
//            messageHelper.setTo(notificationEmail.getRecepient());
//            messageHelper.setSubject(notificationEmail.getSubject());
//            messageHelper.setText(notificationEmail.getBody());
//        };
//        try {
//            mailSender.send(messagePreparator);
//            log.info("Activation email sent!!");
//        } catch (MailException e) {
//            log.error("Exception occurred when sending mail", e);
//            throw new ActivationException("Error sending activation email to " + notificationEmail.getRecepient());
//        }
//    }
//
//}