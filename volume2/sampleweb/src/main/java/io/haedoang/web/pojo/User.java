package io.haedoang.web.pojo;

/**
 * fileName : User
 * author : haedoang
 * date : 2022-06-13
 * description :
 */
public class User {
    private String name;
    private Team team;

    public void setName(String name) {
        this.name = name;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", team=" + team +
                '}';
    }
}
