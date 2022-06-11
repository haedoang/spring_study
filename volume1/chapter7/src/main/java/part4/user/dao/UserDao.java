package part4.user.dao;


import part4.user.domain.User;

import java.util.List;

/**
 * fileName : UserDao
 * author : haedoang
 * date : 2022-03-21
 * description :
 */
public interface UserDao {
    void add(User user);

    User get(String id);

    List<User> getAll();

    void deleteAll();

    int getCount();

    void update(User user);
}
