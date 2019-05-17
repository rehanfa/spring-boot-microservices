package com.code.challange.proxy.security;

import com.code.challange.proxy.common.SecurityTokenConstants;
import com.code.challange.proxy.common.SecurityToken;
import com.code.challange.proxy.service.ProxyService;
import com.code.challange.proxy.service.impl.ProxyServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SecurityFilter extends GenericFilterBean {
    @Autowired
    private ProxyService proxyService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;

        String header = httpServletRequest.getHeader(SecurityTokenConstants.HEADER);

        if (header != null && header.startsWith(SecurityTokenConstants.PREFIX)) {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(httpServletRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);

    }
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityTokenConstants.HEADER);
        token = token.substring(SecurityTokenConstants.PREFIX.length());
        if (token != null) {
            try {
                SecurityToken securityToken = this.getSecurityService(request).hasAccess(token, "/private");
                String username = securityToken.getUserName();
                List<GrantedAuthority> authorities = new ArrayList<>();
                if(securityToken.getRoles() != null) {
                    securityToken.getRoles().forEach(s -> {
                        authorities.add(new SimpleGrantedAuthority(s));
                    });
                }

                if (username != null) {
                    return new UsernamePasswordAuthenticationToken(username, securityToken.getPassword(), authorities);
                }

            } catch (IllegalArgumentException exception) {
                LOGGER.error("empty token", token, exception.getMessage());
            }
        }

        return null;
    }

    public ProxyService getSecurityService(HttpServletRequest request) {
        if(proxyService == null) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            proxyService = webApplicationContext.getBean(ProxyServiceImpl.class);
        }
        return proxyService;
    }

}
