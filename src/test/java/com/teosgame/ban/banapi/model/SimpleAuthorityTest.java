package com.teosgame.ban.banapi.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.teosgame.ban.banapi.model.enums.RoleType;

@SpringBootTest
public class SimpleAuthorityTest {

    @Test
    public void test_Constructor() {
        SimpleAuthority auth = new SimpleAuthority(RoleType.ADMIN);
        assertTrue(auth.getAuthority().equals(RoleType.ADMIN.toString()));
    }

    @Test
    public void test_Equals_Admin() {
        assertTrue(SimpleAuthority.SimpleAdmin().equals(SimpleAuthority.SimpleAdmin()));
    }

    @Test
    public void test_Equals_Dev() {
        assertTrue(SimpleAuthority.SimpleDev().equals(SimpleAuthority.SimpleDev()));
    }

    @Test
    public void test_Equals_User() {
        assertTrue(SimpleAuthority.SimpleUser().equals(SimpleAuthority.SimpleUser()));
    }

    @Test
    public void test_Equals_False() {
        assertFalse(SimpleAuthority.SimpleAdmin().equals(SimpleAuthority.SimpleUser()));
    }
}
