package com.niyogroup.taskmanagement.dto.request;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserRequest {
    @Size(min = 2, max = 11)
    @NotBlank(message = "Firstname is required")
    private String firstname;

    @Size(min = 2, max = 11)
    @NotBlank(message = "Lastname is required")
    private String lastname;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 4, max = 11)
    private String password;
}
