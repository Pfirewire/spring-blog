package com.codeup.springblog.utils;

import com.codeup.springblog.models.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {

    public static Long currentUserId() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }
}
