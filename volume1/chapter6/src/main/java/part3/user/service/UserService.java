package part3.user.service;

import part3.user.domain.User;

/**
 * author : haedoang
 * date : 2022/02/27
 * description :
 */
public interface UserService {
    void add(User user);

    void upgradeLevels();
}
