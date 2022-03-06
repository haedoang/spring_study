package part5.user.dao;

import part5.user.domain.User;

import java.util.List;

/**
 * packageName : part5.user.dao
 * fileName : UserDao
 * author : haedoang
 * date : 2022-03-05
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
