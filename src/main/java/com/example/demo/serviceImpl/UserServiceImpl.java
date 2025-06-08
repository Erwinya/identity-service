package com.example.demo.serviceImpl;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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


    @Override
    public UserDTO getUserByUsername(String username){
        if (username.isBlank() )  {
            throw new BadRequestException("Username cannot be null or empty!");
        }
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found!");
        }                                          

        return UserMapper.toDTO(user.get());

    }
}
