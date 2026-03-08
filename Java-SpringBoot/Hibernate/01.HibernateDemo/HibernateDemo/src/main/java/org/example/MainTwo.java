package org.example;
//done
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class MainTwo {
    public static void main(String[] args) {
        //create a object
        Student s1=new Student();
        s1.setsName("Mahima");
        s1.setsAge(19);
        s1.setRollNo(85);

        //Create SessionFactory to open Session
        SessionFactory sf=new Configuration()
                .configure()
                .buildSessionFactory();

        //Open session
        Session session=sf.openSession();

        //begin transaction
        Transaction tr= session.beginTransaction();

        //use session -> interface for java app and jdbc
        session.save(s1);//save method is deprecated
        session.persist(s1);//this will also give a


        /*Read function in session*/
        Student readResult=session.find(Student.class, 89);//we have to specify which table or here
        //in java which entity/class that table is mapped to fetch data cause session is mapped to a database
        //not a table there can be multiple table and multiple session
        System.out.println(readResult);



        //Updating a record->first find ->set different parameter value ->merge
        if(readResult != null){
            readResult.setsAge(18);
            readResult.setsName("Shivansh Kumar");
            session.merge(readResult);
            System.out.println("Updated");
        }

        //Done with synchronization
        //commit done > t-relisation`
        tr.commit();
        session.close();
        sf.close();
        System.out.println(readResult);
    }
}
