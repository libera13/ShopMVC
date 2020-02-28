package com.example.garage.services;

import com.example.garage.model.User;

public interface UserService {
    public User findUserByEmail(String email);
    public void saveUser(User user);
}

