package sg.edu.nus.imovin.Retrofit.Object;

/**
 * Created by wcafricanus on 18/12/18.
 */

import java.io.Serializable;
import java.util.List;

public class SocialFeedData implements Serializable{
    private String id;
    private String ownerName;
    private String ownerId;
    private String imageString;
    private String message;
    private String createdAt;
    private List<CommentData> comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageString(){
        return imageString;
    }

    public void setImageString(String imageString){
        this.imageString = imageString;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CommentData> getComments() {
        return comments;
    }

    public void setComments(List<CommentData> comments) {
        this.comments = comments;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}

