package com.example.adventuremaps.FireBaseEntities;

public class ClsUser {

    private String userId;
    private String nickName;
    private String email;
    private String password;

    public ClsUser() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ClsUser(String userId, String nickName, String email, String password) {
        this.userId = userId;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }

    //Gets
    public String getUserId() {
        return userId;
    }

    public String getNickName() {
        return nickName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}