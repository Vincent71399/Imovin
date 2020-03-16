package sg.edu.nus.imovin2.Event;

public class EditSocialCommentEvent {
    private String comment_id;

    public EditSocialCommentEvent(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_id() {
        return comment_id;
    }

}
