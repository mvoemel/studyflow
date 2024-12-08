package ch.zhaw.studyflow.webserver.http.cookies;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CookieTest {

    @Test
    void testCookieCreation() {
        Cookie cookie = new Cookie("name", "value");
        assertEquals("name", cookie.getName());
        assertEquals("value", cookie.getValue());
        assertEquals("/", cookie.getPath());
        assertTrue(cookie.isHttpOnly());
    }

    @Test
    void testSetAndGetAttributes() {
        Cookie cookie = new Cookie("name", "value");
        cookie.setMaxAge(3600);
        cookie.setSecure(true);
        cookie.setDomain("example.com");
        cookie.setPath("/test");
        cookie.setSameSite(SameSitePolicy.STRICT);
        LocalDateTime expires = LocalDateTime.now().plusDays(1);
        cookie.setExpires(expires);

        assertEquals(3600, cookie.getMaxAge());
        assertTrue(cookie.isSecure());
        assertEquals("example.com", cookie.getDomain());
        assertEquals("/test", cookie.getPath());
        assertEquals(SameSitePolicy.STRICT, cookie.getSameSite());
        assertEquals(expires, cookie.getExpires());
    }

    @Test
    void testToString() {
        Cookie cookie = new Cookie("name", "value");
        cookie.setMaxAge(3600);
        cookie.setSecure(true);
        cookie.setDomain("example.com");
        cookie.setPath("/test");
        cookie.setSameSite(SameSitePolicy.STRICT);
        LocalDateTime expires = LocalDateTime.now().plusDays(1);
        cookie.setExpires(expires);

        String expected = "name=value; Expires=" + expires.format(DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss 'GMT'")) +
                "; Max-Age=3600; Domain=example.com; Path=/test; Secure; HttpOnly; SameSite=Strict";
        assertEquals(expected, cookie.toString());
    }


    @Test
    void testReadFromHeaderInvalid() {
        String header = "invalid-header";
        Optional<Cookie> cookieOpt = Cookie.readFromHeader(header);
        assertFalse(cookieOpt.isPresent());
    }

    @Test
    void testCookieParsing() {
        String header = "name=value; Expires=Wed, 21 Oct 2015 07:28:00 GMT; Max-Age=3600; Domain=example.com; Path=/test; Secure; HttpOnly; SameSite=Strict";
        Optional<Cookie> cookieOpt = Cookie.readFromHeader(header);
        assertTrue(cookieOpt.isPresent());
        Cookie cookie = cookieOpt.get();
        assertEquals("name", cookie.getName());
        assertEquals("value", cookie.getValue());
        assertEquals(3600, cookie.getMaxAge());
        assertTrue(cookie.isSecure());
        assertEquals("example.com", cookie.getDomain());
        assertEquals("/test", cookie.getPath());
        assertEquals(SameSitePolicy.STRICT, cookie.getSameSite());
        assertEquals(LocalDateTime.of(2015, 10, 21, 7, 28), cookie.getExpires());
    }

    @Test
    void testMissingArgument() {
        String header = "name=value; Expires=Wed, 21 Oct 2015 07:28:00 GMT; Max-Age=; Domain=example.com; Path=/test; Secure; HttpOnly";
        assertThrows(IllegalArgumentException.class, () -> Cookie.readFromHeader(header));
    }
}