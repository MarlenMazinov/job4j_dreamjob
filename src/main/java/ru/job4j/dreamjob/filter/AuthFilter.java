package ru.job4j.dreamjob.filter;

import org.springframework.stereotype.Component;
import ru.job4j.dreamjob.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.UnknownServiceException;
import java.util.Set;

@Component
public class AuthFilter implements Filter {
    private final Set<String> permittedUris = Set.of("loginPage", "login", "registration",
            "formRegistration", "success", "fail");

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        User user = (User) req.getSession().getAttribute("user");
        if (checkUri(uri) || (user != null && !user.getEmail().equals("Гость"))) {
            chain.doFilter(req, res);
            return;
        }
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/loginPage");
            return;
        }
        chain.doFilter(req, res);
    }

    private boolean checkUri(String uri) {
        String[] arr = uri.split("/");
        return permittedUris.contains(arr[arr.length - 1]);
    }
}