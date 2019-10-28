package sg.edu.nus.imovin.Retrofit.Object;

public class LikeData {
    private Boolean like_by_me;
    private Integer likes;

    public Boolean getLike_by_me() {
        return like_by_me;
    }

    public void setLike_by_me(Boolean like_by_me) {
        this.like_by_me = like_by_me;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }
}
