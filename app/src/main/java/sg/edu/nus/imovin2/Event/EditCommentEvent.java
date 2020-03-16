package sg.edu.nus.imovin2.Event;

public class EditCommentEvent {
    private String comment_id;

    public EditCommentEvent(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_id() {
        return comment_id;
    }

}
