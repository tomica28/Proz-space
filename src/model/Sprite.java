package model;

import javafx.scene.image.ImageView;

public class Sprite extends ImageView{
    private boolean dead;
    private String type;
    public boolean isMovingLeft;
    public boolean isMovingRight;
    private double speed;
    public Sprite(String image, String type) {
        super(image);
        dead = false;
        this.type = type;
        speed = 3;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
    public boolean getDead() { return dead; }
    public String getType() { return type;}
    public void setSpeed(double speed) { this.speed = speed; }

    public void moveLeft() { setLayoutX(getLayoutX() - speed); }

    public void moveRight() {
        setLayoutX(getLayoutX() + speed);
    }

    public void moveUp() {
        setLayoutY(getLayoutY() - speed);
    }

    public void moveDown() {
        setLayoutY(getLayoutY() + speed);
    }
}
