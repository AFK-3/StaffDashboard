package id.ac.ui.cs.advprog.staffdashboard.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter @Getter
public class User {
    private UUID id;
    private String username;
    private String password;
    private String name;
    private String adress;
    private String phoneNumber;
    private String type;
}
