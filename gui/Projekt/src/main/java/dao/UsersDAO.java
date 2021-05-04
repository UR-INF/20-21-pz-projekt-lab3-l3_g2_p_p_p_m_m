package main.java.dao;

import main.java.controllers.auth.Encryption;
import main.java.entity.UserInfos;
import main.java.entity.Users;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class UsersDAO {

    static public List<Users> getUsers(){
        Session session = HibernateUtil.getSessionFactory().openSession();

        Query query = session.createQuery("from Users");

        List<Users> listOfUsers = query.list();

        return listOfUsers;
    }

    static public List<Users> getUsersId(int id){
        Session session = HibernateUtil.getSessionFactory().openSession();

        Query query = session.createQuery("FROM Users WHERE userInfoId = :id");
        query.setParameter("id",id);

        List<Users> listOfUsers = query.list();

        return listOfUsers;
    }

    static public void updatePassword(int userId, String newPassword){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Users users = session.get(Users.class, userId);

        users.setPassword(newPassword);

        session.update(users);

        session.getTransaction().commit();
    }

    static public List<UserInfos> readUserInfoById(int userId){
        Session session = HibernateUtil.getSessionFactory().openSession();

        Query query=session.createQuery("from Users u where u.userInfoId = :id");
        query.setParameter("id",userId);
        List<Users> id = query.list();

        query=session.createQuery("from UserInfos u where u.id = :id");
        query.setParameter("id", id.get(0).getUserInfoId());
        List<UserInfos> user = query.list();

        return user;
    }

    static public boolean deleteAccount(String password, int id){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query=session.createQuery("SELECT password from Users WHERE password = :password AND id = :id");
        query.setParameter("password", Encryption.encrypt(password));
        query.setParameter("id", id);

        Query query1 = session.createQuery("SELECT PH.status FROM PackageHistory PH, Users U, Packages P " +
                "WHERE U.id = :id AND PH.packageId = P.id AND U.id = P.userId AND PH.status = " +
                "(SELECT PH.status FROM PackageHistory PH WHERE PH.id = (SELECT MAX(PH.id) " +
                "FROM PackageHistory PH WHERE PH.packageId = P.id )) AND " +
                "NOT PH.status = 'Dostarczona' AND NOT PH.status = 'Zwrócona Do Nadawcy' GROUP BY PH.packageId");

        query1.setParameter("id", id);

        if(query.list().size() == 0 || query1.list().size() != 0)
            return false;

        return true;
    }

}