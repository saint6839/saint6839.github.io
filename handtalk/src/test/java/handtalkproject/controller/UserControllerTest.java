package handtalkproject.controller;

import handtalkproject.domain.dto.UserSignUpDto;
import handtalkproject.domain.entity.User;
import handtalkproject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                                 .build();
    }

    @Test
    @DisplayName("사용자 회원가입이 잘 되는지 테스트")
    void create() throws Exception {
        User user = createUser();
        when(userService.save(any()))
                .thenReturn(user);

        mockMvc.perform(post("/users/")
                                .param("userId", user.getUserId())
                                .param("password", user.getPassword())
                                .param("name", user.getName())
                                .param("email", user.getEmail()))
               .andDo(print())
               .andExpect(status().isOk());
    }

    User createUser() {
        return new User("userId1", "password1", "name1", "email1");
    }

}