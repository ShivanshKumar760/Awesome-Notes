package org.example;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Departments {
    @Id
    private int id;
    private String dept_name;

    @OneToMany(mappedBy = "departments")
    private List<Employees> employees;
    //Suppose i have dept d1 and i have employees e1,e2,e3 in it since id is primary key
    //i cant have
    //d1-e1
    //d1-e2
    //d1-e3 as d1 should be unique


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public List<Employees> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employees> employees) {
        this.employees = employees;
    }

}
