package com.example.demo.serviceImpl;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public String addNewUser(UserDTO userDTO) {
        User user = UserMapper.toEntity(userDTO);
        if (user == null) {
            throw new IllegalArgumentException("User is not valid");
        }
        userRepository.save(user);
        return "User added successfully!";
    }
}
