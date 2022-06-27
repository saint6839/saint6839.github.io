package handtalkproject.domain.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String profile;
    private boolean isEmailAuthorized;

    @Builder
    public User(String email, String password, String nickname, String profile, boolean isEmailAuthorized) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profile = profile;
        this.isEmailAuthorized = isEmailAuthorized;
    }

    public User() {

    }
}
