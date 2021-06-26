package com.gurmeet.corvac;

public class usersignupdataholder {
    String fullname,phonenumber,email,aadharnumber;

    public usersignupdataholder(String fullname, String phonenumber, String email, String aadharnumber) {
        this.fullname = fullname;
        this.phonenumber = phonenumber;
        this.email = email;
        this.aadharnumber = aadharnumber;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAadharnumber() {
        return aadharnumber;
    }

    public void setAadharnumber(String aadharnumber) {
        this.aadharnumber = aadharnumber;
    }
}
