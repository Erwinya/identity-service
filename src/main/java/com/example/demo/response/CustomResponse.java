package com.example.demo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomResponse<T> {
    @JsonProperty("data")
    private T data;

    @JsonProperty("status_code")
    private Integer statusCode;

    @JsonProperty("status_message")
    private String statusMessage;

    @JsonProperty("timestamp")
    private String timestamp;
}
