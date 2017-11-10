package janie.firebasedemo;

/**
 * Created by Dell on 08-Nov-17.
 */

public class FriendlyMessage {
    private String text;
    private String name;
    private String photoUrl;

    public FriendlyMessage() {
    }

    public FriendlyMessage(String text, String name, String photoURL) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoURL;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
