package com.talentbuilder.talentbuilder.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConstants {
	
    @Value("${app.contact-email-address}")
    public String CONTACT_EMAIL_ADDRESS;

    @Value("${app.url}")
    public String APP_URL;

    @Value("${app.name}")
    public String APP_NAME;

    @Value("${app.description}")
    public String APP_DESCRIPTION;

    @Value("${app.api-license-url}")
    public String APP_LICENSE_URL;

    @Value("${app.api-author}")
    public String APP_AUTHOR;

    @Value("${app.api-web-url}")
    public String APP_WEB_URL;

    @Value("${app.api-email}")
    public String APP_EMAIL;
    
    @Value("${app.api-version}")
    public String APP_VERSION;

    @Value("${security.jwt.client-id}")
    public String CLIENT_ID;
    
    @Value("${security.jwt.client-secret}")
    public String CLIENT_SECRET;
    
    @Value("${security.jwt.grant-type}")
    public String GRANT_TYPE;
    
    @Value("${app.login-url}")
    public String APP_LOGIN_URL;

    @Value("${app.product-name}")
    public String PRODUCT_NAME;
    
    @Value("${app.admin-email}")
    public String APP_ADMIN_EMAIL;
    
    @Value("${app.admin-password}")
    public String APP_ADMIN_PASSWORD;
    
    @Value("${app.admin-name}")
    public String APP_DEFAULT_ADMIN_NAME;
    
    @Value("${app.admin-phone}")
    public String APP_DEFAULT_ADMIN_PHONE;

    @Value("${mail.username}")
    public String MAIL_USERNAME;

    private AppConstants() {
    }

}
