package org.example;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;



public class Main {
    public static void main(String[] args) throws HibernateException {

//       Student s1=new Student();
//       s1.setsName("Mahima");
//       s1.setsAge(19);
//       s1.setRollNo(85);
       //First mount the Configuration
//        Configuration cfg=new Configuration();
//        cfg.configure();
//        then we need session factory which we need to build from Configuration
//        SessionFactory sf=cfg.buildSessionFactory();//this is a heavy object so use only per database in program
          SessionFactory sf=new Configuration()
                  .configure()
                  .buildSessionFactory();
          //Session session=null;//so if we need a hibernate session we need to take it from session factory
          Session session=sf.openSession();//openSession Method returns a session
          Transaction tr= session.beginTransaction();
          //session.save(s1);//save method is deprecated
//         session.persist(s1);//this will also give a
            Student readResult=session.find(Student.class, 89);
            if(readResult != null){
               readResult.setsAge(18);
               readResult.setsName("Shivansh Kumar");
               session.merge(readResult);
               System.out.println("Updated");
            }
            readResult=session.find(Student.class,89);
        System.out.println(readResult);
            //delete
            session.remove(readResult);
        readResult=session.find(Student.class,89);
          tr.commit();
          session.close();
          sf.close();
          System.out.println(readResult);
    }
}