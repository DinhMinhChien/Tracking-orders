package com.example.trackingorders.config;

import com.example.trackingorders.common.RoleEnum;
import com.example.trackingorders.entity.Users;
import com.example.trackingorders.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomizeUserDetailService implements UserDetailsService {
    private final UsersRepository usersRepository ;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepository.findByUsername(username) ;
        if(users == null) {
            throw new UsernameNotFoundException("Not found username !") ;
        }
        RoleEnum role = users.getRole() ;
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role) ;
        return new User(username,users.getPassword(), List.of(authority)) ;
    }
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder() ;
        System.out.printf(passwordEncoder.encode("123456"));
    }
}
