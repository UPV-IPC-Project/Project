/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv;

import model.User;

/**
 * @file userSession.java
 * Keeps the data constant between different scenes.
 * @author Alesan Price aprice
 */
public class userSession {
    private static final userSession instance = new userSession();
    private User user;
    private userSession(){
        
    }
    public static userSession getInstance(){
        return instance;
    }
    public User getUser(){
        return user;
    }
    public void setUser(User u){
        user = u;
    }
    
}
