package com.talentbuilder.talentbuilder.mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
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

    public void sendSimpleMessage(Mail mail) throws MessagingException, IOException, TemplateException {
        ((JavaMailSenderImpl)this.emailSender).setPassword(OAuthMail.getAccessToken());

        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");
        Template t = freemarkerConfig.getTemplate(mail.getTemplate());
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, mail.getModel());

        helper.setTo(mail.getTo());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        System.out.println("PRINT SENDER INFORMATION: "+sender);
        helper.setFrom(sender);
        emailSender.send(message);
    }

    public void sendMessage(String from, String to, String subject, String msg) {
        ((JavaMailSenderImpl)this.emailSender).setPassword(OAuthMail.getAccessToken());
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(msg);

        // sending email

        emailSender.send(message);
    }

}
