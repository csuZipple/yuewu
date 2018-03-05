package zippler.cn.yuewu.entitiy;

/**
 * Created by zipple on 2018/3/5.
 */

public class User {
    private String username;
    private String avatar;
    private String description;

    public User(){

    }

    public User(String username, String avatar, String description) {
        this.username = username;
        this.avatar = avatar;
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
