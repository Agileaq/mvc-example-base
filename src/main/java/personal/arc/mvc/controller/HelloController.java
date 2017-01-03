package personal.arc.mvc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.arc.mvc.bean.Person;

import javax.servlet.http.*;

/**
 * Created by Arc on 18/8/2016.
 */
@RestController
@RequestMapping("say")
public class HelloController {

    @RequestMapping("hello")
    public String sayHello(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        HttpSession httpSession = httpServletRequest.getSession();
        String id = httpSession.getId();
        System.out.println(id);
        String CURRENT_SESSION_ATTR = HttpServletRequestWrapper.class.getName();

        httpServletRequest.setAttribute(CURRENT_SESSION_ATTR, "可以将current session给换掉, 但必须是从redis拿出来的HttpSessionWrapper(该类型为private)类型的");
        // HttpSession httpSession2 = httpServletRequest.getSession();
        return "Hello world!";
    }

    @RequestMapping(value = "contact")
    public Person getInfo(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        return new Person().setEmail("286999915@qq.com").setName("钱晟龙");
    }

}
