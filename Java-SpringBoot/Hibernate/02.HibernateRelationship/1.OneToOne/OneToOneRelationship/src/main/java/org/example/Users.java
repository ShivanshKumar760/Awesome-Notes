package org.example;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Users {
    @Id
    private int uid;
    private String uname;
    private int age;
    //We need to establish the relationship b/w User and Passport table/class,since each user
    //can have one passport and each passport can be occupied by one user its one to one relationship
    //now we can have fkey in any table but we will go with User table to set fkey
    @OneToOne
    private Passport passport;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Passport getPassport() {
        return passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", uname='" + uname + '\'' +
                ", age=" + age +
                ", passport=" + passport +
                '}';
    }
}
