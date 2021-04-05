package com.mywarehouse.inventory.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * @description: Use CachedBodyHttpServletRequest for reading request body multiple times instead HttpServletRequest
 * @author: Suresh_Vannale
 */
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
public class LoggingFilter extends OncePerRequestFilter {

    @Value("${spring.profiles.active}")
    private String profile;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    private static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
            MediaType.valueOf("text/*"),
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML,
            MediaType.valueOf("application/*+json"),
            MediaType.valueOf("application/*+xml"),
            MediaType.MULTIPART_FORM_DATA
    );


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        addMdcParams();
        if (StringUtils.isEmpty(profile) || profile.equalsIgnoreCase("dev")) {
            doFilterWrapped(wrapRequest(httpServletRequest), wrapResponse(httpServletResponse), filterChain);
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
        long totalTime = System.currentTimeMillis() - startTime;
        MDC.put("timeTakenInMilli", "timeTakenInMilli=" + totalTime);
        LOGGER.info("Time taken to finish request is [{}] milliseconds", totalTime);
        MDC.clear();
    }

    protected void doFilterWrapped(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws ServletException, IOException {
        try {
            beforeRequest(request, response);
            filterChain.doFilter(request, response);
        } finally {
            afterRequest(request, response);
            response.copyBodyToResponse();
        }
    }

    protected void beforeRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        if (LOGGER.isInfoEnabled()) {
            logRequestHeader(request, request.getRemoteAddr() + "|>");
        }
    }

    private static void logRequestHeader(ContentCachingRequestWrapper request, String prefix) {
        String queryString = request.getQueryString();
        if (queryString == null) {
            LOGGER.info("{} {} {}", prefix, request.getMethod(), request.getRequestURI());
        } else {
            LOGGER.info("{} {} {}?{}", prefix, request.getMethod(), request.getRequestURI(), queryString);
        }
        Collections.list(request.getHeaderNames()).forEach(headerName ->
                Collections.list(request.getHeaders(headerName)).forEach(headerValue ->
                        LOGGER.info("{} {}: {}", prefix, headerName, headerValue)));
        LOGGER.info("{}", prefix);
    }

    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }

    public void addMdcParams() {
        MDC.put("transactionId", "transactionId =" + UUID.randomUUID().toString());
        MDC.put("timeTakenInMilli", "timeTakenInMilli=NA");
    }

    private static void logResponse(ContentCachingResponseWrapper response, String prefix) {
        int status = response.getStatus();
        LOGGER.info("{} {} {}", prefix, status, HttpStatus.valueOf(status).getReasonPhrase());
        response.getHeaderNames().forEach(headerName ->
                response.getHeaders(headerName).forEach(headerValue ->
                        LOGGER.info("{} {}: {}", prefix, headerName, headerValue)));
        LOGGER.info("{}", prefix);
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            logContent(content, response.getContentType(), response.getCharacterEncoding(), prefix);
        }
    }

    protected void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        if (LOGGER.isInfoEnabled()) {
            logResponse(response, request.getRemoteAddr() + "|<");
        }
    }

    private static void logContent(byte[] content, String contentType, String contentEncoding, String prefix) {
        MediaType mediaType = MediaType.valueOf(contentType);
        boolean visible = VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
        if (visible) {
            try {
                String contentString = new String(content, contentEncoding);
                Stream.of(contentString.split("\r\n|\r|\n")).forEach(line -> LOGGER.info("{} {}", prefix, line));
            } catch (UnsupportedEncodingException e) {
                LOGGER.info("{} [{} bytes content]", prefix, content.length);
            }
        } else {
            LOGGER.info("{} [{} bytes content]", prefix, content.length);
        }
    }
}
