package kyocoolcool.takeshoes.controller;

import kyocoolcool.takeshoes.entity.Person;
import kyocoolcool.takeshoes.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chris Chen
 * @version 1.0
 * @className PersonController
 * @description
 * @date 2020/5/6 11:09 AM
 **/

@Controller
public class LoginController {

    @RequestMapping("/login")
    public Object login() {
        return "/login";
    }
}
