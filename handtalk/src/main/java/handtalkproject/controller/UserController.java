package handtalkproject.controller;

import handtalkproject.domain.dto.UserSignUpDto;
import handtalkproject.domain.entity.User;
import handtalkproject.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public User create(UserSignUpDto userSignUpDto) {
        User save = userService.save(userSignUpDto.toEntity());
        return save;
    }
}
