package part3recap.user.dao;

import part3recap.user.domain.User;

import java.util.List;

/**
 * packageName : part4.user.dao
 * fileName : UserDao
 * author : haedoang
 * date : 2022-02-28
 * description :
 */
public interface UserDao {
    void add(User user);

    User get(String id);

    List<User> getAll();

    void deleteAll();

    int getCount();

    void update(User user1);
}
