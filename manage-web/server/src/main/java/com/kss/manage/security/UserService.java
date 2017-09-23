package com.kss.manage.security;

import com.kss.manage.po.UserPo;
import com.kss.manage.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserPo user = userRepository.findByUsername(username);
        if(user == null) {
            logger.error("找不到用户：{}", username);
            new UsernameNotFoundException("找不到用户：" + username);
        }
        return user;
    }

    public boolean save(UserPo user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //TODO 待删除
        logger.info("pwd：{}", user.getPassword());
        return userRepository.save(user) != null;
    }
}
