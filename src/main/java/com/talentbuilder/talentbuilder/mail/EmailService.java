package com.talentbuilder.talentbuilder.mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    @Autowired
    private Configuration freemarkerConfig;

    @Autowired
    private JavaMailSender emailSender;

    @Value("${mail.from}")
    private String sender;

	@Value("${mail.password}")
	private String password;

	private Logger logger= LoggerFactory.getLogger(this.getClass());

    public void sendSimpleMessage(Mail mail) throws MessagingException, IOException, TemplateException {
//        ((JavaMailSenderImpl)this.emailSender).setPassword(OAuthMail.getAccessToken());

        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");
        Template t = freemarkerConfig.getTemplate(mail.getTemplate());
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, mail.getModel());

        helper.setTo(mail.getTo());
        helper.setFrom(new InternetAddress(sender));
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        System.out.println("PRINT SENDER INFORMATION: "+sender+" and TOKEN: "+OAuthMail.getAccessToken());
//        helper.setFrom(sender);
        emailSender.send(message);
    }

    public void sendMessage(Mail mail) {
        ((JavaMailSenderImpl)this.emailSender).setPassword(OAuthMail.getAccessToken());
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(sender);
        message.setTo(mail.getTo());
        message.setSubject(mail.getSubject());
        message.setText(mail.getModel().toString());
        System.out.println("About sending mail....");
        // sending email

        emailSender.send(message);
    }

    public void send(Mail mail) throws MailException {
        MimeMessagePreparator messagePreparator = (MimeMessage mimeMessage) -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(sender);
            messageHelper.setTo(mail.getTo());
            messageHelper.setSubject(mail.getSubject());
            messageHelper.setText(mail.getModel().toString());
        };

        logger.info("Trying to send mail to {}", mail.getTo());

        emailSender.send(messagePreparator);
        logger.info("Email successfully sent to {} with subject '{}'", mail.getTo(), mail.getModel());
    }


}
