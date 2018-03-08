/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iRetrieve.cloud.service.impl;

/**
 *
 * @author Alfie
 */
import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.iRetrieve.cloud.domain.Role;
import com.iRetrieve.cloud.domain.User;
import com.iRetrieve.cloud.domain.VerificationToken;
import com.iRetrieve.cloud.repository.RoleRepository;
import com.iRetrieve.cloud.repository.UserRepository;
import com.iRetrieve.cloud.repository.VerificationTokenRepository;
import com.iRetrieve.cloud.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserByUserId(int user_id) {
        return userRepository.findByUserId(user_id);
    }

    @Override
    public void saveUser(User user) {
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void registerNewUserAccount(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(false);
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    @Override
    public User getUser(String verificationToken) {
        User user = tokenRepository.findByToken(verificationToken).getUser();
        return user;
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public VerificationToken getVerificationToken(User user) {
        return tokenRepository.findByUser(user);
    }

}
