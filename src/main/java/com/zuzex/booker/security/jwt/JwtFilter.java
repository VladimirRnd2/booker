package com.zuzex.booker.security.jwt;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static io.jsonwebtoken.lang.Strings.hasText;

@Component
@Log
public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION = "authorization";

    @Autowired
    private JwtProv jwtProvider;

    @Autowired
    private UserDetailsService jwtUserService;

    @Autowired
    public JwtFilter(JwtProv jwtProvider, UserDetailsService jwtUserService) {
        this.jwtProvider = jwtProvider;
        this.jwtUserService = jwtUserService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);
        if (token != null && jwtProvider.validateToken(token)) {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                String userLogin = jwtProvider.getLoginFromAccessToken(token);
                JwtUser jwtUser = (JwtUser) jwtUserService.loadUserByUsername(userLogin);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            else {
                SecurityContextHolder.getContext().setAuthentication(null);
            }
        }
        else {

        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }


}
