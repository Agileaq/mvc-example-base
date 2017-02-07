package personal.arc.mvc.jwt.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.web.http.SessionRepositoryFilter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by Arc on 11/1/2017.
 */
public class SpringSessionRepositoryWrapperFilter implements Filter {


    public static Boolean isEnabledSpringSession = Boolean.TRUE;

    @Autowired
    private SessionRepositoryFilter sessionRepositoryFilter;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(isEnabledSpringSession != null && isEnabledSpringSession) {
            sessionRepositoryFilter.doFilter(request, response, chain);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }

}
