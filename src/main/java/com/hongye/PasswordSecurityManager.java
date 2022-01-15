package com.hongye;

public class PasswordSecurityManager extends SecurityManager{
    private String password;

    PasswordSecurityManager(String password) {
        super();
        this.password = password;
    }
    public boolean accessOK(String password) {
        if(this.password.equals(password))  return true;
        else return false;
    }
}
