package models;

import lombok.Data;

@Data
public class UserResponse {
    private User data;
    private Support support;
}
