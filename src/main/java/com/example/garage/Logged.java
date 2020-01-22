package com.example.garage;

import com.example.garage.model.User;

public class Logged {
    private User loggedUser;
    private boolean logged;

    public Logged() {
        logged = false;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }
}
