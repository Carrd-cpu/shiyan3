package com.shiyan3.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class SessionUtil {
    public static final String USER_ID = "userId";
    public static final String USER_ROLE = "role";
    public static final String USERNAME = "username";

    private SessionUtil() {
    }

    public static Integer getUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return null;
        }
        Object val = session.getAttribute(USER_ID);
        return val instanceof Integer ? (Integer) val : null;
    }

    public static String getRole(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return null;
        }
        Object val = session.getAttribute(USER_ROLE);
        return val == null ? null : val.toString();
    }
}
