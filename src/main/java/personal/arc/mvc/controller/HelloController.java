package personal.arc.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.arc.mvc.bean.Person;
import personal.arc.mvc.jwt.filter.SpringSessionRepositoryWrapperFilter;

import javax.servlet.http.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arc on 18/8/2016.
 */
@RestController
@RequestMapping("say")
public class HelloController {

    @Autowired
    private RedisOperationsSessionRepository sessionRepository;

    @RequestMapping(value = "login")
    public Person login(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        return new Person().setEmail("286999915@qq.com").setName("钱晟龙");
    }

    @RequestMapping("hello")
    public String sayHello(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        HttpSession httpSession = httpServletRequest.getSession();
        String id = httpSession.getId();
        System.out.println(id);
        String CURRENT_SESSION_ATTR = HttpServletRequestWrapper.class.getName();
//        httpServletRequest.setAttribute(CURRENT_SESSION_ATTR, "可以将current session给换掉, 但必须是从redis拿出来的HttpSessionWrapper(该类型为private)类型的");
//        HttpSession httpSession2 = httpServletRequest.getSession();

        return "Hello world!";
    }

    @RequestMapping(value = "contact")
    public Person getInfo(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        Map map = new HashMap();
        Cookie[] cookies = httpServletRequest.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                map.put(cookie.getName(), cookie.getValue());
            }
        }
        return new Person().setEmail("286999915@qq.com").setName(map.toString());
    }

    @RequestMapping("switch")
    public void switchFilter(){
        SpringSessionRepositoryWrapperFilter.isEnabledSpringSession = SpringSessionRepositoryWrapperFilter.isEnabledSpringSession ? false : true;
    }

    @RequestMapping("set-cookie")
    public String setCookie(HttpServletResponse response) {
        Cookie cookie1 = new Cookie("test-111", "test-111111");
        // http://这里是domain/这里是path
        cookie1.setPath("/");
        response.addCookie(cookie1);

        Cookie cookie2 = new Cookie("test-111", "test-222222");
        cookie2.setDomain("test.com");
        cookie2.setPath("/");
        response.addCookie(cookie2);

        Cookie cookie3 = new Cookie("test-111", "test-333333");
        response.addCookie(cookie3);
        return "done";
    }

    @RequestMapping("remove-cookie1")
    public String remove1(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie1 = new Cookie("test-111", null);
        // http://这里是domain/这里是path
        cookie1.setPath("/");
        cookie1.setMaxAge(0);
        response.addCookie(cookie1);
        return "done";
    }

    @RequestMapping("remove-cookie2")
    public String remove2(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie1 = new Cookie("test-111", null);
        cookie1.setDomain("test.com");
        cookie1.setPath("/");
        cookie1.setMaxAge(0);
        response.addCookie(cookie1);
        return "done";
    }

    @RequestMapping("remove-cookie3")
    public String remove3(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie1 = new Cookie("test-111", null);
        cookie1.setMaxAge(0);
        response.addCookie(cookie1);
        return "done";
    }

}
