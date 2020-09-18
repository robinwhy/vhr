package org.why.myvhr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.why.myvhr.mapper.HrMapper;
import org.why.myvhr.model.Hr;
@Service
public class HrService implements UserDetailsService {
    @Autowired
    HrMapper hrMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Hr user=hrMapper.loadUserByUsername(s);
        if(user==null){throw new UsernameNotFoundException("Username not found!");}
        return user;
    }
}
