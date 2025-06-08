package com.example.demo.service;

import com.example.demo.dto.UserDTO;

public interface UserService {
    public String addNewUser(UserDTO userDTO);
    public UserDTO getUserByUsername(String username);
}
