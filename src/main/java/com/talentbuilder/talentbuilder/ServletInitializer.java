package com.talentbuilder.talentbuilder;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;


@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(TalentbuilderApplication.class);
	}

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

//    @Bean
//    public ServletRegistrationBean dispatcherServletRegistration() {
//        Collection<String> mappings = new ArrayList<String>();
//        mappings.add("/*");
//        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet());
//        registration.setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);
//        registration.setUrlMappings(mappings);
//        return registration;
//    }

    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){ return new BCryptPasswordEncoder();}

}

