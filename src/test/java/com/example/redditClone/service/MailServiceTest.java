package com.example.redditClone.service;

import com.example.redditClone.exception.ActivationException;
import com.example.redditClone.models.NotificationEmail;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;

import static org.junit.Assert.assertEquals;

@SpringBootTest()
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class MailServiceTest {

    @Resource
    private JavaMailSender javaMailSender;

    private NotificationEmail notificationEmail;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private GreenMail greenMail;


    @Before
    public void mailSetUp() {

        greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();
    }

    @After
    public void after() {
        greenMail.stop();
    }

    @Before
    public void setUp() {
        notificationEmail = new NotificationEmail(
                "Account Activation",
                "daniel@gmail.com", "Welcome to React-Spring-Reddit Clone");
    }


    @Test()
    public void testSendEmailSuccesful() throws InterruptedException, MessagingException {

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springreddit@email.com");
            messageHelper.setTo(notificationEmail.getRecepient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(notificationEmail.getBody());
        };

        try {
            javaMailSender.send(messagePreparator);
        } catch (MailSendException e) {
            throw new ActivationException("Error sending activation email to "
                    + notificationEmail.getRecepient());
        }

        Message[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);
    }

    @Test(expected = ActivationException.class)
    public void testActivationException() {

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springreddit@email.com");
            messageHelper.setText(notificationEmail.getBody());
        };

        try {
            javaMailSender.send(messagePreparator);
        } catch (MailSendException e) {
            throw new ActivationException("Error sending activation email to "
                    + notificationEmail.getRecepient());
        }
    }
}

