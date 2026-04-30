package org.example;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Passport {
    @Id
    private int passport_id;
    private String country;

    public int getPassport_id() {
        return passport_id;
    }

    public void setPassport_id(int passport_id) {
        this.passport_id = passport_id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Passport{" +
                "passport_id=" + passport_id +
                ", country='" + country + '\'' +
                '}';
    }
}
