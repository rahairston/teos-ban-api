package com.teosgame.ban.banapi.filters;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import com.teosgame.ban.banapi.client.RedisClient;

@Configuration
public class SessionResponseFilter {

  @Autowired
  private RedisClient redisClient;

  private final String JSESSION = "JSESSIONID=";
  private final String anon = "anonymousUser";
  
  Logger logger = LoggerFactory.getLogger(SessionResponseFilter.class);

  @Bean
  public Filter sessionFilter() {
      return (request, response, chain) -> {
          chain.doFilter(request, response);
          // We do this for the rare occasion we Set-Cookie on a new call
          if (response instanceof HttpServletResponse) {
              HttpServletResponse httpRes = (HttpServletResponse) response;
              httpRes.getHeaders("Set-Cookie").stream().forEach(header -> {
                  int index = header.indexOf(JSESSION);
                  String user = SecurityContextHolder.getContext()
                      .getAuthentication().getName();
                  if (index != -1 && !user.equals(anon)) {
                      int sessionIdStart = index + JSESSION.length();
                      int sessionIdEnd = header.indexOf(";", sessionIdStart);
                      String newSessionId = header.substring(sessionIdStart, sessionIdEnd);
                      redisClient.setValue(user, newSessionId);
                  }
              });
          }
      };
  }
}
