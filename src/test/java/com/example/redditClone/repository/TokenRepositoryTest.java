//package com.example.redditClone.repository;
//
//import com.example.redditClone.models.AccountVerificationToken;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.Optional;
//import java.util.UUID;
//
//@SpringBootTest()
//@RunWith(SpringRunner.class)
//@ActiveProfiles("test")
//public class TokenRepositoryTest {
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    @Autowired
//    private TokenRepository tokenRepository;
//
//    @MockBean
//    private JavaMailSender sender;
//
//    @Test
//    public void tokenVerificationReturnsTrueIfTokenIsValid(){
//        String token = UUID.randomUUID().toString();
//        AccountVerificationToken verificationToken = new AccountVerificationToken();
//        verificationToken.setToken(token);
//        tokenRepository.save(verificationToken);
//
//        Optional<AccountVerificationToken> resultToken = tokenRepository.findByToken(token);
//
//        Assert.assertNotNull(resultToken.get());
//
//    }
//
//}
