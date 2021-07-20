package com.example.minorproject.Model;

public class User {
    private String Name;
    private String Password;
    private String Email;
    private String EmergencyPhone;

    public User() {
    }
    public User(String email,String name,String password,String Ephone) {
        Name = name;
        Password=password;
        Email=email;
        EmergencyPhone=Ephone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmergencyPhone() {
        return EmergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        EmergencyPhone = emergencyPhone;
    }
}
