package hung.testdraganddrop;

/**
 * Created by everything on 3/30/2016.
 */
public class nav_item {
    private String title;
    private int resIcon;

    public nav_item(String title, int resIcon) {
        this.title = title;
        this.resIcon = resIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }
}
