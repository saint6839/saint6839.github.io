package handtalkproject.service;

import handtalkproject.domain.entity.User;
import handtalkproject.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userService)
                                 .build();
    }

    @Test
    @DisplayName("사용자 회원가입이 잘 되는지 테스트")
    void save() throws Exception {
        //given
        User user = createUser();

        //when
        when(userRepository.save(user)).thenReturn(user);
        User savedUser = userService.save(user);

        //then
        Assertions.assertThat(savedUser)
                  .isEqualTo(user);
    }

    User createUser() {
        return new User("userId1", "password1", "name1", "email1", false);
    }
}