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

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("")
    public User create(UserSignUpDto userSignUpDto) {
        User save = userService.save(userSignUpDto.toEntity());
        return save;
    }

    @GetMapping("")
    @ResponseBody
    public void test() throws MessagingException, UnsupportedEncodingException {
        emailService.sendSimpleMessage("hyeonji0718@gmail.com", "손말잇기 회원가입 인증코드입니다.");
    }
}
