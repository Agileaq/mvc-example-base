package personal.arc.mvc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.arc.mvc.bean.Person;

/**
 * Created by Arc on 18/8/2016.
 */
@RestController
@RequestMapping("say")
public class HelloController {

    @RequestMapping("hello")
    public String sayHello() {
        return "Hello world!";
    }

    @RequestMapping(value = "contact")
    public Person getInfo() {
        return new Person().setEmail("286999915@qq.com").setName("钱晟龙");
    }

}
