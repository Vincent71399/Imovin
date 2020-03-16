package sg.edu.nus.imovin2.Activities;

import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.REQUEST_CREATE_SOCIAL_COMMENT;
import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.REQUEST_SOCIAL_COMMENT_WITH_ID;

public class NewSocialCommentActivity extends NewCommentActivity{
    @Override
    protected void SetData(){
        CREATE_COMMENT_STRING = REQUEST_CREATE_SOCIAL_COMMENT;
        EDIT_COMMENT_STRING = REQUEST_SOCIAL_COMMENT_WITH_ID;
    }
}
