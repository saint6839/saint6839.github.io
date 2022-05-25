package handtalkproject.controller;

import handtalkproject.domain.dto.UserSignUpDto;
import handtalkproject.domain.entity.User;
import handtalkproject.service.EmailService;
import handtalkproject.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private EmailService emailService;

    @PostMapping("")
    public User create(UserSignUpDto userSignUpDto) {
        User save = userService.save(userSignUpDto.toEntity());
        return save;
    }

    @GetMapping("")
    @ResponseBody
    public void test() {
        emailService.sendSimpleMessage("hyeonji0718@gmail.com", "맨지야", "바보");
    }
}
