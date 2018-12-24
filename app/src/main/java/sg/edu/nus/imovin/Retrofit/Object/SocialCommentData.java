package sg.edu.nus.imovin.Retrofit.Object;

/**
 * Created by wcafricanus on 19/12/18.
 */

public class SocialCommentData extends CommentData {
    private String socialPostId;

    public String getThreadId() {
        return socialPostId;
    }

    public void setThreadId(String threadId) {
        this.socialPostId = threadId;
    }
}
