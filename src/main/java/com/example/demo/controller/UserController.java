package com.example.demo.controller;

import com.example.demo.response.CustomResponse;
import com.example.demo.dto.UserDTO;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomResponse<String> registerNewUser(@RequestBody UserDTO userDTO) {
        String serviceResponse = userService.addNewUser(userDTO);
        CustomResponse<String> response = new CustomResponse<>();
        response.setData(serviceResponse);
        response.setStatusCode(200);
        response.setStatusMessage("SUCCESS");
        response.setTimestamp(Instant.now().toString());
        return response;
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse<UserDTO> getUserByUsername(@RequestParam String username) {
        UserDTO userDTO = userService.getUserByUsername(username);
        CustomResponse<UserDTO> response = new CustomResponse<>();
        response.setData(userDTO);
        response.setStatusCode(200);
        response.setStatusMessage("SUCCESS");
        response.setTimestamp(Instant.now().toString());
        return response;
    }
}
