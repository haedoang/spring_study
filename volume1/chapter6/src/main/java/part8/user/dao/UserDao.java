package part8.user.dao;

import part8.user.domain.User;

import java.util.List;

/**
 * fileName : UserDao
 * author : haedoang
 * date : 2022-03-06
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
