package com.juhnkim.service;

public class LoginService {
    private boolean isUserLogged = false;

    public boolean isUserLogged() {
        return isUserLogged;
    }

    public void setUserLogged(boolean userLogged) {
        isUserLogged = userLogged;
    }
}
