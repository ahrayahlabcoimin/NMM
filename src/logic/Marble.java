package logic;


import java.awt.*;

public class Marble {
    private Point position = new Point(-1,-1);

    public void setPosition(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }
}
