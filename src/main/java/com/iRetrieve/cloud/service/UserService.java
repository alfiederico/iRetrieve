/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iRetrieve.cloud.service;

import com.iRetrieve.cloud.domain.User;


/**
 *
 * @author Alfie
 */
public interface UserService {
	public User findUserByEmail(String email);
        public User findUserByUserId(int user_id);
	public void saveUser(User user);
}
