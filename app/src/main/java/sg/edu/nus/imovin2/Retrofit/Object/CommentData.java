package sg.edu.nus.imovin2.Retrofit.Object;

import java.io.Serializable;

public class CommentData implements Serializable {
    private String _id;
    private String created_at;
    private Boolean liked_by_me;
    private Integer likes;
    private String message;
    private String parent_node;
    private String updated_at;
    private String user_id;
    private String user_name;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Boolean getLiked_by_me() {
        return liked_by_me;
    }

    public void setLiked_by_me(Boolean liked_by_me) {
        this.liked_by_me = liked_by_me;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getParent_node() {
        return parent_node;
    }

    public void setParent_node(String parent_node) {
        this.parent_node = parent_node;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
