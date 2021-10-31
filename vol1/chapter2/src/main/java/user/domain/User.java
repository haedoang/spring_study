package user.domain;

/**
 * packageName : user.domain
 * fileName : User
 * author : haedoang
 * date : 2021/10/31
 * description : User class
 */
public class User {
    private String id;
    private String name;
    private String password;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
