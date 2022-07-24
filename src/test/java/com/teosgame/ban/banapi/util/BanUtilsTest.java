package com.teosgame.ban.banapi.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.teosgame.ban.banapi.model.SimpleAuthority;
import com.teosgame.ban.banapi.model.UserAuth;

@SpringBootTest
public class BanUtilsTest {

    @Autowired
    private BanUtils utils;

    @Mock
    SecurityContext context;

    UserAuth user;

    @BeforeEach
    public void beforeTests() {
        SecurityContextHolder.setContext(context);
    }

    @Test
    public void test_isUserAdmin_True() {
        List<SimpleAuthority> auth = new ArrayList<>();
        auth.add(SimpleAuthority.SimpleAdmin());
        user = new UserAuth(auth, "", "null", "null", true);
        when(context.getAuthentication()).thenReturn(user);
        assertTrue(utils.isUserAdmin());
    }

    @Test
    public void test_isUserAdmin_False() {
        List<SimpleAuthority> auth = new ArrayList<>();
        auth.add(SimpleAuthority.SimpleUser());
        user = new UserAuth(auth, "", "null", "null", true);
        when(context.getAuthentication()).thenReturn(user);
        assertFalse(utils.isUserAdmin());
    }
    
}
