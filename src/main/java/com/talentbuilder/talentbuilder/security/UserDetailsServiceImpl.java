package com.talentbuilder.talentbuilder.security;


import com.talentbuilder.talentbuilder.model.Privilege;
import com.talentbuilder.talentbuilder.model.User;
import com.talentbuilder.talentbuilder.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService accountService;

    private static Logger logger = LogManager.getLogger(UserDetailsServiceImpl.class);

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String emailOrphone) throws UsernameNotFoundException {

        logger.info("NOW STARTING AUTHENTICATION AT USER DETAILS SERVICE");

        User account = accountService.findByUsernameOrEmail(emailOrphone);
        
        if(account == null){
        	throw new UsernameNotFoundException("user not exists");
        }
        
        logger.info("DONE WITH AUTHENTICATION AT USER DETAILS SERVICE ");

        return new org.springframework.security.core.userdetails.User(emailOrphone, account.getPassword(), getAuthorities(account));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User role) {
        return getGrantedAuthorities(getPrivileges(role));
    }

    private List<String> getPrivileges(User role) {
        List<String> privileges = new ArrayList<String>();
        List<Privilege> collection = new ArrayList<Privilege>();
        collection.addAll(role.getPrivileges());

        for (Privilege item : collection) {
            privileges.add(item.getName().toString());
        }

        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

}