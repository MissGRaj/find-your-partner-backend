package com.example.find_your_partner.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger log =
            LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    private static final String START_TIME =
            "requestStartTime";

    private static final String REQUEST_ID =
            "requestID";

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ){

        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();

        request.setAttribute(START_TIME, startTime);
        request.setAttribute(REQUEST_ID, requestId);

        response.setHeader("X-Request-ID", requestId);

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        Object userId = "anonymous";

        if(authentication != null && authentication.isAuthenticated()){

            userId = authentication.getPrincipal();
        }

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {

        Object startAttribute = request.getAttribute(START_TIME);

        if (!(startAttribute instanceof Long startTime)) {
            return;
        }

        long durationMs = System.currentTimeMillis() - startTime;

        String requestId = (String) request.getAttribute(REQUEST_ID);

    }
}
