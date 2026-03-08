package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Arrays;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Members m1=new Members();
        m1.setMemid(1);
        m1.setMemberName("Shivansh");


        Members m2=new Members();
        m2.setMemid(2);
        m2.setMemberName("Abinav");

        Members m3=new Members();
        m3.setMemid(3);
        m3.setMemberName("Mahima");


        Members m4=new Members();
        m4.setMemid(4);
        m4.setMemberName("Mike");

        Societies s1=new Societies();
        s1.setSid(1);
        s1.setS_name("Saviour Park");

        Societies s2=new Societies();
        s2.setSid(2);
        s2.setS_name("Gulmohar");


        m1.setSocieties(Arrays.asList(s1,s2));
        m2.setSocieties(Arrays.asList(s1));
        m3.setSocieties(Arrays.asList(s1,s2));
        m4.setSocieties(Arrays.asList(s2));

        s1.setMembers(Arrays.asList(m1,m2,m3));
        s2.setMembers(Arrays.asList(m1,m3,m4));



        System.out.println(m1);
        System.out.println(m2);
        System.out.println(m3);
        System.out.println(m4);

        Configuration cfg=new Configuration();
        SessionFactory sf=cfg.configure().buildSessionFactory();
        Session session=sf.openSession();
        Transaction tr=session.beginTransaction();
        //we need to save passport first cause passport id will be used as foreign key
        //if we save user first then foreign key will be left empty

        session.persist(m1);
        session.persist(m2);
        session.persist(m3);
        session.persist(m4);
        session.persist(s1);
        session.persist(s2);

        tr.commit();
        session.close();
        sf.close();
    }
}