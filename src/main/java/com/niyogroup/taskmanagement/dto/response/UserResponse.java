package com.niyogroup.taskmanagement.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
public class UserResponse {

    private String firstname;

    private String lastname;

    private String email;

    private String password;

    private LocalDateTime createdAt;
}
