package com.example.garage.utilities;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtills {
    public static String getLoggedUser(){
        String currentUserName = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)){
            currentUserName = auth.getName();
        }
        return currentUserName;
    }
}
