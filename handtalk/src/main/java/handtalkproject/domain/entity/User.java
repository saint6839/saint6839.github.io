package handtalkproject.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String userId;
    private String password;
    private String name;
    private String email;
    private boolean isEmailAuthorized;

    public User(String userId, String password, String name, String email, boolean isEmailAuthorized) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.isEmailAuthorized = isEmailAuthorized;
    }
}
