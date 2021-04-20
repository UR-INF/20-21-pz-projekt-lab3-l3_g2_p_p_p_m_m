package main.java.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.entity.PackageType;
import main.java.entity.Packages;
import main.java.entity.UserInfos;
import main.java.entity.Users;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import java.util.List;

public class UsersDAO {
    static public void addUser(int user_infoID, String password, int areaID, String role){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        Users user = new Users();
        user.setUserInfoId(user_infoID);
        user.setPassword(password);
        user.setAreaId(areaID);
        user.setRole(role);

        session.save(user);

        transaction.commit();

        session.close();
    }


    static public String readPassword(int userId){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query=session.createQuery("SELECT password from Users WHERE id = :id");
        query.setParameter("id",userId);

        return String.valueOf(query.list().get(0));
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
        List<Users> listOfTypeInfo = query.list();

        query=session.createQuery("from UserInfos u where u.id = :id");
        query.setParameter("id", id.get(0).getUserInfoId());
        List<UserInfos> user = query.list();

        return user;
    }

}
