package uz.pdp.projectimtihon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
    private String verificationCode;
    private String verificationCodeForPassword;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User() {

    }

}
