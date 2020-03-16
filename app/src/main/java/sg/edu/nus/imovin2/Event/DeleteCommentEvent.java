package sg.edu.nus.imovin2.Event;

public class DeleteCommentEvent {
    private String comment_id;

    public DeleteCommentEvent(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_id() {
        return comment_id;
    }

}
