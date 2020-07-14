package in.technostack.projects.converterforall.DataModels;

import java.io.Serializable;

public class ImageItem implements Serializable {
    private int image;
    private String title;
    private int link;
    private int position;

    public ImageItem(int image, String title, int link, int position) {
        super();
        this.image = image;
        this.title = title;
        this.link=link;
        this.position = position;
    }
    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public int getLink() {
        return link;
    }
    public int getPosition() {
        return position;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setPosition(String position) {
        this.title = position;
    }
}
