package model;

import javafx.scene.image.ImageView;

public class Sprite extends ImageView{
    private boolean dead;
    private String type;

    public Sprite(String image, String type) {
        super(image);
        dead = false;
        this.type = type;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
    public boolean getDead() { return dead; }
    public String getType() { return type;}

    public void moveLeft() { setLayoutX(getLayoutX() - 3); }

    public void moveRight() {
        setLayoutX(getLayoutX() + 3);
    }

    public void moveUp() {
        setLayoutY(getLayoutY() - 3);
    }

    public void moveDown() {
        setLayoutY(getLayoutY() + 3);
    }
}
