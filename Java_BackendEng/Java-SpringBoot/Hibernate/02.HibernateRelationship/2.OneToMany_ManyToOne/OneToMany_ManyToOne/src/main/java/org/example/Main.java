package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.lang.reflect.Array;
import java.util.Arrays;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Employees e1=new Employees();
        e1.setEmpid(2814986);
        e1.setEmp_name("Shivansh");
        e1.setEmp_age(19);
        e1.setBadgeNumber(789);

        Employees e2=new Employees();
        e2.setEmpid(2814987);
        e2.setEmp_name("Arpit");
        e2.setEmp_age(19);
        e2.setBadgeNumber(783);

        Employees e3=new Employees();
        e3.setEmpid(2804986);
        e3.setEmp_name("Himalay");
        e3.setEmp_age(26);
        e3.setBadgeNumber(781);

        Departments d1=new Departments();
        d1.setDept_name("Backend");
        d1.setId(1);

        Departments d2=new Departments();
        d2.setDept_name("Frontend");
        d2.setId(2);

        d1.setEmployees(Arrays.asList(e1,e3));
        d2.setEmployees(Arrays.asList(e2));

        e1.setDepartments(d1);
        e2.setDepartments(d2);
        e3.setDepartments(d1);



        System.out.println(e1);
        System.out.println(e2);
        System.out.println(e3);

        System.out.println(d1);
        System.out.println(d2);

        Configuration cfg=new Configuration();
        SessionFactory sf=cfg.configure().buildSessionFactory();
        Session session=sf.openSession();
        Transaction tr=session.beginTransaction();
        //we need to save passport first cause passport id will be used as foreign key
        //if we save user first then foreign key will be left empty

        session.persist(e1);
        session.persist(e2);
        session.persist(e3);
        session.persist(d1);
        session.persist(d2);

        tr.commit();
        session.close();
        sf.close();
    }
}