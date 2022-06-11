package user.dao;

import user.domain.User;

import java.util.List;

/**
 * author : haedoang
 * date : 2022/02/19
 * description :
 */
public interface UserDao {
    void add(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();
}