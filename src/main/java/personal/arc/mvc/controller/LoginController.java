package personal.arc.mvc.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.arc.mvc.bean.Person;
import personal.arc.mvc.jwt.AuthPrivateKeyGetter;
import personal.arc.mvc.jwt.AuthPublicKeyGetter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Arc on 3/1/2017.
 */
@RestController
@RequestMapping("login")
public class LoginController {

    public static final String JWT_COOKIE_NAME = "auth_jwt";
    private String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKBLn2pA0pAlJymGYelv2NcdjFiZk3Lq5yXywH_aPnGYbmqgdxAWSnW_Kr-zbkNj8jJyyB23-Y9UTH_cGY77BNbmcD2A6AeBQnpXOJZ5azA9Tx5TyK1_wzbWCrYYxDb2bW_kr4h4Qjrl2ltBvz0KrsBgqMirT2gea2meJlxTGFSbAgMBAAECgYBGToUAbKgucy1HKFr_sYARcjNhlRK4P9Hcsim-qjHjTzF28STn2G8WUB_m70N4qXjMqc2eQ1hhhl77YcWQEgefrMVURYfTcxcKz7H1NUsXsi8hmdSvg_R7UPOeW08FQvgAnaHuwaf7t_Xt5hGpc8FiHUx4znKzdg1lU2AsJ45ryQJBAMrXDQmStBjeYZGRaIG9261HRoWsDfK5F0-j0T0Osn2bTcS1KSROC1w3FV7gRCqbBGwg6YHnw7SnkpzdXh5Ygy0CQQDKTippUIaU8VzsHqKdXBT42H4EJnYPwqRaQ8-Xc24-MOMHprBpMBVa1N8F62sAKp-elWhibwaqShxW5uhaeDPnAkEAw5NyuKZBDDFvdgHz-BTtuTeeIcjxcfVRKrLTTsHbDA0wgNgEIUM25OUfr-khEMuDChb5zw1-v7NLRACVfUDuyQJBAJ-s6F6tVzwR8WTQnUsUldz_ix8deEviNjSklyIT0qy1i0vLriun7wL9R3Z1pMPB4LCbfoNK3Hjl-84_fJ-DhnsCQQCyZ3mL4GATDaDVIfazmaTg_YNbL-hu8Z3P0ovlsfUgxe294AAT1LK5-2Pzd2jNUO7u0dAZ0BjNOwLU6gw1000q";
    private AuthPrivateKeyGetter authPrivateKeyGetter = new AuthPrivateKeyGetter(privateKey);

    @RequestMapping(value = "password")
    public Person getInfo(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        if("123".equals(httpServletRequest.getParameter("password"))) {
            Long aid = 1L;
            Long now = new Date().getTime();
            String jsonWebToken = JWT.create()
                    .withJWTId(UUID.randomUUID().toString().replaceAll("-", "").toLowerCase())
                    .withIssuer("local")
                    .withExpiresAt(new Date(now + 1000*60*120L))
                    .withAudience("store", "forum", "www", "local")
                    .withSubject("authentication")
                    .withClaim("aid", aid.toString())
                    .sign(Algorithm.RSA256(authPrivateKeyGetter.getRsaPrivateKey()));

            Cookie cookie = new Cookie(JWT_COOKIE_NAME, jsonWebToken);
            // http://这里是domain/这里是path
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            return new Person().setEmail("286999915@qq.com").setName("Chinese Name - 钱晟龙").setAid(aid);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "logout")
    public String logout(HttpServletRequest httpServletRequest, HttpServletResponse response){
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, null);
        // http://这里是domain/这里是path
//        cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "success";
    }
}
