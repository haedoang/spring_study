package part4.user.domain;

/**
 * packageName : part4.user.domain
 * fileName : User
 * author : haedoang
 * date : 2022/02/05
 * description :
 */
public class User {
    String id;
    String name;
    String password;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
