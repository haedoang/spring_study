package part3.user.dao;


import part3.user.domain.User;

import java.util.List;

/**
 * fileName : UserDao
 * author : haedoang
 * date : 2022-03-11
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
