package ultra.com.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;

@Controller
@CrossOrigin("*")
@AllArgsConstructor
public class AppController {
    
    @GetMapping("/{path:[^\\.]*}")
    public String redirect() {
        return "forward:/";
    }

    @GetMapping("/")
    public String index() {
        System.out.println("index");
        return "index.html";
    }

}
