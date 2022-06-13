package io.haedoang.web.main;

import io.haedoang.web.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * fileName : MainController
 * author : haedoang
 * date : 2022-06-13
 * description :
 */
@Controller
@RequestMapping
public class MainController {
    private final User user;

    public MainController(User user) {
        this.user = user;
    }

    @GetMapping
    public String main(Model model) {
        model.addAttribute("user", user);
        return "index";
    }
}
