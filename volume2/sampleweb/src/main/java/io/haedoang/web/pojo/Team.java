package io.haedoang.web.pojo;

/**
 * fileName : Team
 * author : haedoang
 * date : 2022-06-13
 * description :
 */
public class Team {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                '}';
    }
}
