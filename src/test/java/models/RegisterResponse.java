package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
public class RegisterResponse {
    private int id;
    private String token;
}