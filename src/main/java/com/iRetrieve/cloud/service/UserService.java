/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iRetrieve.cloud.service;

import com.iRetrieve.cloud.domain.User;
import com.iRetrieve.cloud.domain.VerificationToken;
import java.util.List;

/**
 *
 * @author Alfie
 */
public interface UserService {

    public User findUserByEmail(String email);

    public User findUserByUserId(int user_id);

    public void saveUser(User user);

    public void registerNewUserAccount(User user);

    User getUser(String verificationToken);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    VerificationToken getVerificationToken(User user);
    
    List<User> findAllByOrderByUserIdAsc();
}
