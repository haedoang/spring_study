package part3.user.service;

import org.springframework.transaction.annotation.Transactional;
import part3.user.domain.User;

import java.util.List;

/**
 * author : haedoang
 * date : 2022/03/06
 * description :
 */
@Transactional
public interface UserService {

    void add(User user);

    @Transactional(readOnly = true)
    User get(String id);

    @Transactional(readOnly = true)
    List<User> getAll();

    void deleteAll();

    void update(User user);

    void upgradeLevels();
}
