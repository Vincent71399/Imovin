package sg.edu.nus.imovin.Retrofit.Object;

public class LikeData {
    private Boolean liked_by_me;
    private Integer likes;

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
}
