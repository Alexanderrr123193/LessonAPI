package models;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Lombok {
    private String name;
    private String job;
    private String id;
    private String email;
    private String password;
    private Data data;
    private String token;

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private int id;
        private String email;
        private String firstName;
        private String lastName;
        private String avatar;
    }
}
