package syuu.service.VO;

public class CommentVo {
    private int id;
    private UserVo fromUser;
    private UserVo toUser;
    private String yd;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserVo getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserVo fromUser) {
        this.fromUser = fromUser;
    }

    public UserVo getToUser() {
        return toUser;
    }

    public void setToUser(UserVo toUser) {
        this.toUser = toUser;
    }

    public String getYd() {
        return yd;
    }

    public void setYd(String yd) {
        this.yd = yd;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
