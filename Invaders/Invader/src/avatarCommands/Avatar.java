package avatarCommands;

import java.awt.Image;

public class Avatar {

    private boolean v;
    private Image i;
    private boolean d;

    int x;
    int y;
    int dx;

    public Avatar() {

        v = true;
    }

    public void die() {

        v = false;
    }

    public boolean isVisible() {

        return v;
    }

    protected void setVisible(boolean v) {

        this.v = v;
    }

    public void setImage(Image i) {

        this.i = i;
    }

    public Image getImage() {

        return i;
    }

    public void setX(int x) {

        this.x = x;
    }

    public void setY(int y) {

        this.y = y;
    }

    public int getY() {

        return y;
    }

    public int getX() {

        return x;
    }

    public void setDying(boolean d) {

        this.d = d;
    }

    public boolean isDying() {

        return this.d;
    }
}
