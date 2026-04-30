package com.acgist.health.monitor.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.acgist.health.monitor.configuration.HealthProperties;

@WebFilter(urlPatterns = {"/monitor"})
public class MonitorFilter implements Filter {

    @Autowired
    private HealthProperties healthProperties;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(
            request  instanceof HttpServletRequest         &&
            response instanceof HttpServletResponse        &&
            this.healthProperties.getSecurity() != null    &&
            !this.healthProperties.getSecurity().isEmpty() &&
            !this.healthProperties.getSecurity().equals(((HttpServletRequest) request).getHeader("token"))
        ) {
            ((HttpServletResponse) response).setStatus(401);
            return;
        }
        chain.doFilter(request, response);
    }

}
