//package com.example.redditClone.service;
//
//import com.example.redditClone.models.NotificationEmail;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.mail.javamail.MimeMessagePreparator;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import static org.mockito.Mockito.mock;
//
//import javax.mail.Session;
//import javax.mail.internet.MimeMessage;
//
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//
//import static org.mockito.Mockito.times;
//
//@SpringBootTest()
//@RunWith(SpringRunner.class)
//@ActiveProfiles("test")
//public class MailServiceTest {
//
//    @Mock
//    private JavaMailSender javaMailSender;
//
//    @Autowired
//    private MailBuilder mailBuilder;
//
//    private NotificationEmail notificationEmail;
//
//    @Autowired
//    MailService mailService;
//
//    @Before
//    public void setUp() {
//
//        String message1 = mailBuilder.build("Welcome to React-Spring-Reddit Clone");
//
//        notificationEmail = new NotificationEmail(
//                "Account Activation",
//                "daniel@gmail.com", message1);
//    }
//
////    @Before
////    public void before() {
////        mimeMessage = new MimeMessage((Session)null);
//////        javaMailSender = mock(JavaMailSender.class);
//////        Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
////    }
////        @Test
////    public void  testSendMailMethod() throws Exception {
////        MimeMessagePreparator messagePreparator = mock(MimeMessagePreparator.class);
////        MimeMessage mimeMessage = mock(MimeMessage.class);
////        MimeMessageHelper mimeMessageHelper= new MimeMessageHelper(mimeMessage);
////        mailService.
////
////        Mockito.doNothing().when(messagePreparator).prepare(mimeMessage);
////
//////        Mockito.doNothing().when(javaMailSender).send(Mockito.any(MimeMessagePreparator.class));
//////        messagePreparator.prepare(mimeMessage);
////        mailService.sendEmail(notificationEmail);
////        Mockito.verify(messagePreparator, times(1)).prepare(mimeMessage);
////
////    }
//
//    @Test
//    public void testSendMimeMessagePreparatorObject() throws Exception {
//        Mockito.doNothing().when(javaMailSender).send(Mockito.any(MimeMessagePreparator.class));
//        mailService.sendEmail(notificationEmail);
//
////        Assert.assertEquals("Activation Email Sent", outContent.toString());
//        Mockito.verify(javaMailSender, times(1)).send(Mockito.any(MimeMessagePreparator.class));
//
////        Mockito.verify(messagePreparator, times(1)).prepare(Mockito.any(MimeMessage.class));
////        Mockito.verify(mimeMessageHelper, times(1)).setSubject(notificationEmail.getRecepient());
////        Assert.assertNotNull(MimeMessageHelper mimeMessageHelper.getMimeMessage());
//
//
//    }
//}
//
