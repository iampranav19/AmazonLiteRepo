package com.pranav.dto;

import lombok.*;
import org.springframework.http.HttpStatus;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ApiResponse {

    private String message;

    private HttpStatus status;

    private boolean success;
}
