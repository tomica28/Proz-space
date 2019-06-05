package model;

import javafx.scene.image.ImageView;
import org.apache.log4j.Logger;

public class Sprite extends ImageView{
    private boolean dead;
    private String type;
    public boolean isMovingLeft;
    public boolean isMovingRight;
    private double speed;
    private int lives;

    private Logger logger = Logger.getLogger(Sprite.class);

    public Sprite(String image, String type) {
        super(image);
        dead = false;
        this.type = type;
        speed = 3;
        lives = 1;
        logger.info("##Sprite created " + type + " lives " + lives + " speed " + speed);
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
    public boolean getDead() { return dead; }
    public String getType() { return type;}
    public void setSpeed(double speed) { this.speed = speed; }
    public void setLives(int lives) {
        this.lives = lives;
    }
    public int getLives() { return  lives; }

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
