package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Users u1=new Users();
        u1.setUid(101);
        u1.setAge(19);
        u1.setUname("Shivansh");

        Passport p1=new Passport();
        p1.setPassport_id(2814986);
        p1.setCountry("India");

        u1.setPassport(p1);

        System.out.println(u1);

        Configuration cfg=new Configuration();
        SessionFactory sf=cfg.configure().buildSessionFactory();
        Session session=sf.openSession();
        Transaction tr=session.beginTransaction();
        //we need to save passport first cause passport id will be used as foreign key
        //if we save user first then foreign key will be left empty

        session.persist(p1);
        session.persist(u1);

        tr.commit();
        session.close();
        sf.close();
    }
}