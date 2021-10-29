package user.domain;

/**
 * packageName : user.domain
 * fileName : User
 * author : haedoang
 * date : 2021/10/29
 * description : 사용자 정보 저장용 Bean
 */
public class User {

    String id;
    String name;
    String password;

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
