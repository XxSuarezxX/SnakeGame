package view;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Obstacle {
    private int x;
    private int y;
    private int width;
    private int height;

    public Obstacle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

}