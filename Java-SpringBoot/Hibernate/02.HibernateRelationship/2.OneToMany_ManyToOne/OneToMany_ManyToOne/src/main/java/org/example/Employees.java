package org.example;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Employees {
    @Id
    private int empid;
    private int badgeNumber;
    private String emp_name;
    private int emp_age;
    @ManyToOne
    private Departments departments;

    public int getEmpid() {
        return empid;
    }


    //Suppose i have dept d1 and i have employees e1,e2,e3 in d1 in it since id is primary key
    //i cant have
    //e1-d1
    //e2-d1
    //e3-d1 as d1 is foreign key and can be repeated but emp_id cant and it not being
    //repeated so it does not violate the code

    public void setEmpid(int empid) {
        this.empid = empid;
    }

    public int getBadgeNumber() {
        return badgeNumber;
    }

    public void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public int getEmp_age() {
        return emp_age;
    }

    public void setEmp_age(int emp_age) {
        this.emp_age = emp_age;
    }

    public Departments getDepartments() {
        return departments;
    }

    public void setDepartments(Departments departments) {
        this.departments = departments;
    }

    @Override
    public String toString() {
        return "Employees{" +
                "empid=" + empid +
                ", badgeNumber=" + badgeNumber +
                ", emp_name='" + emp_name + '\'' +
                ", emp_age=" + emp_age +
                ", departments=" + departments +
                '}';
    }
}
