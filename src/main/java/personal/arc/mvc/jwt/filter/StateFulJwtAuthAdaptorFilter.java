package personal.arc.mvc.jwt.filter;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import personal.arc.mvc.controller.LoginController;
import personal.arc.mvc.jwt.AuthPublicKeyGetter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Created by Arc on 3/1/2017.
 */
public class StateFulJwtAuthAdaptorFilter implements Filter {

    private String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgS59qQNKQJScphmHpb9jXHYxYmZNy6ucl8sB_2j5xmG5qoHcQFkp1vyq_s25DY_Iycsgdt_mPVEx_3BmO-wTW5nA9gOgHgUJ6VziWeWswPU8eU8itf8M21gq2GMQ29m1v5K-IeEI65dpbQb89Cq7AYKjIq09oHmtpniZcUxhUmwIDAQAB";
    private AuthPublicKeyGetter authPublicKeyGetter = new AuthPublicKeyGetter(publicKey);
    private JWTVerifier verifier = JWT.require(Algorithm.RSA256(authPublicKeyGetter.getRsaPublicKey())).build();
    private final String key = "jwt_sso";
    private String CURRENT_SESSION_ATTR = HttpServletRequestWrapper.class.getName();

    @Autowired
    private RedisOperationsSessionRepository redisOperationsSessionRepository;

    @Autowired
    private RedisTemplate<String, Object> sessionRedisTemplate;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse)resp;

        HttpSession httpSession = request.getSession(false);
        if(httpSession != null) {
            // if the session is already existed, then do nothing.
            chain.doFilter(request, response);
            return;
        }

        String jwt = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(LoginController.JWT_COOKIE_NAME.equals(cookie.getName())){
                    jwt = cookie.getValue();
                }
            }
        }
        if(jwt == null) {
            // if there is no jwt in cookie or header, then do nothing.
            chain.doFilter(request, response);
            return;
        }
        DecodedJWT decodedJWT;
        try {
            decodedJWT = verifier.verify(jwt);
        } catch (JWTVerificationException jwtVerificationException) {
            // if jwt decode failed, then do nothing.
            chain.doFilter(request, response);
            return;
        }

        String jti = decodedJWT.getId();
        HashOperations<String, String, String> hashOperations = sessionRedisTemplate.opsForHash();
        String jSessionId = hashOperations.get(key, jti);
        if(jSessionId == null) {
            HttpSession createdHttpSession = request.getSession();
            String createdHttpSessionId = createdHttpSession.getId();
            // set up session with aid here. authenticate with jwt
            hashOperations.put(key, jti, createdHttpSessionId);
            createdHttpSession.setAttribute("jti", jti);
            createdHttpSession.setAttribute("aid", decodedJWT.getClaim("aid"));
        } else {
            Object redisSession = redisOperationsSessionRepository.getSession(jSessionId);
            if(redisSession != null) {
                request.setAttribute(CURRENT_SESSION_ATTR, redisSession);
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
