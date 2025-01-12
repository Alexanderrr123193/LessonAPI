package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
public class CreateUserResponse {
    private String id;
    private String name;
    private String job;
    private String createdAt;
}