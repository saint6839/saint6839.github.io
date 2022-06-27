package handtalkproject.service;

import handtalkproject.domain.entity.User;
import handtalkproject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("userId1", "password1", "name1", "email1", false);
    }

    @Test
    @DisplayName("사용자 회원가입이 잘 되는지 테스트")
    void signUp() {
        //given
        //when
        User savedUser = userService.save(user);

        //then
        assertThat(user).isEqualTo(savedUser);
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 회원가입을 시도할 때 회원가입이 안되도록 하는지 테스트")
    void duplicatedEmailsignUp() {
        //given
        userService.save(user);

        //when
        User duplicatedUser = new User("userId1", "password1", "name1", "email1", false);

        //then
        assertThatThrownBy(() -> userService.save(duplicatedUser)).isInstanceOf(RuntimeException.class);
    }
}