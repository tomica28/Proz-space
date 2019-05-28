package view;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.SHIP;
import javafx.scene.image.ImageView;
import model.Sprite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameViewManager {
    private AnchorPane GamePane;
    private Stage GameStage;
    private Scene GameScene;

    private static final int GAME_WIDTH = 600;
    private static final int GAME_HEIGHT = 800;
    private double time = 0;
    private double shootingTime = 1;

    private Stage menuStage;

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private boolean isShooting = false;
    private AnimationTimer gameTimer;

    private GridPane gridPane1;
    private GridPane gridPane2;
    private final static String BACKGROUND_IMAGE = "view/resources/purple.png";

    private final static String BLACK_ENEMY_IMAGE = "view/resources/enemyBlack.png";
    private final static String GREEN_ENEMY_IMAGE = "view/resources/enemyGreen.png";
    private List<Sprite> sprites;
    private final static int ENEMY_XPOSITION = 30;
    private final static int ENEMY_YPOSITION = 30;

    private List<Sprite> bullets;
    private final static String BULLET_IMAGE = "view/resources/bullet.png";

    public GameViewManager() {
        initializeStage();
        createKeyListeners();
    }

    private void initializeStage() {
        sprites = new ArrayList<>();
        bullets = new ArrayList<>();
        GamePane = new AnchorPane();
        GameScene = new Scene(GamePane, GAME_WIDTH, GAME_HEIGHT );
        GameStage = new Stage();
        GameStage.setScene(GameScene);
    }

    private void createKeyListeners() {
        GameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case A: isLeftKeyPressed = true; break;
                    case D: isRightKeyPressed = true; break;
                    case SPACE: if(!isShooting && shootingTime > 0.5) {
                        createBullet(sprites.get(0));
                        isShooting = true;
                        shootingTime = 0;
                    }
                    break;
                }
            }
        });

        GameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case A: isLeftKeyPressed = false; break;
                    case D: isRightKeyPressed = false; break;
                    case SPACE: isShooting = false; break;
                }
            }
        });

    }

    public void createNewGame(Stage menuStage, SHIP choosenShip) {
        this.menuStage = menuStage;
        this.menuStage.hide();
        createBackground();
        createShip(choosenShip);
        createEnemies();
        createGameLoop();
        GameStage.show();
    }

    private void createShip(SHIP choosenShip) {
        Sprite ship = new Sprite(choosenShip.getUrl(), "player");
        sprites.add(ship);
        sprites.get(0).setLayoutX(GAME_WIDTH/2);
        sprites.get(0).setLayoutY(GAME_HEIGHT-90);
        GamePane.getChildren().add(sprites.get(0));
    }

    private void createGameLoop() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                moveBackground();
                moveShip();
                shooting();
                endingGame();
            }
        };
        gameTimer.start();
    }

    private void moveShip() {
        if(isLeftKeyPressed && !isRightKeyPressed) {
            if(sprites.get(0).getLayoutX() > 0) {
                sprites.get(0).moveLeft();
            }
        }
        if(!isLeftKeyPressed && isRightKeyPressed) {
            if(sprites.get(0).getLayoutX() < 500) {
                sprites.get(0).moveRight();
            }
        }
    }


    private void createBackground() {
        gridPane1 = new GridPane();
        gridPane2 = new GridPane();

        for(int i = 0; i<12; i++) {
            ImageView backgroundImage1 = new ImageView(BACKGROUND_IMAGE);
            ImageView backgroundImage2 = new ImageView(BACKGROUND_IMAGE);
            GridPane.setConstraints(backgroundImage1, i % 3, i / 3);
            GridPane.setConstraints(backgroundImage2, i % 3, i / 3);
            gridPane1.getChildren().add(backgroundImage1);
            gridPane2.getChildren().add(backgroundImage2);
        }

        gridPane2.setLayoutY(-1024);

        GamePane.getChildren().addAll(gridPane1, gridPane2);
    }

    private void moveBackground() {
        gridPane1.setLayoutY(gridPane1.getLayoutY() + 0.5);
        gridPane2.setLayoutY(gridPane2.getLayoutY() + 0.5);

        if(gridPane1.getLayoutY() >= 1024) {
            gridPane1.setLayoutY(-1024);
        }

        if(gridPane2.getLayoutY() >= 1024) {
            gridPane2.setLayoutY(1024);
        }
    }

    private void createEnemies() {
        //creating first lane of enemies
        for(int i = 0; i < 5; i++) {
            Sprite enemy = new Sprite(BLACK_ENEMY_IMAGE, "enemy");
            GamePane.getChildren().add(enemy);
            enemy.setLayoutX(ENEMY_XPOSITION + 110*i);
            enemy.setLayoutY(ENEMY_YPOSITION);
            sprites.add(enemy);
        }
        //creating second lane of enemies
        for (int i = 0; i < 5; i++) {
            Sprite enemy = new Sprite(GREEN_ENEMY_IMAGE, "enemy");
            GamePane.getChildren().add(enemy);
            enemy.setLayoutY(ENEMY_YPOSITION + 90);
            enemy.setLayoutX(ENEMY_XPOSITION + 110*i);
            sprites.add(enemy);
        }
    }

    private void createBullet(Sprite who) {
        Sprite bullet = new Sprite(BULLET_IMAGE, who.getType() + "bullet");
        bullet.setLayoutX(who.getLayoutX() + 45);
        bullet.setLayoutY(who.getLayoutY());
        GamePane.getChildren().add(bullet);
        bullets.add(bullet);
    }

    private void shooting() {
        time += 0.016;
        shootingTime += 0.016;
        bullets.forEach(s -> {
            switch (s.getType()) {
                case "enemybullet":
                    s.moveDown();

                    if (s.getBoundsInParent().intersects(sprites.get(0).getBoundsInParent())) {
                        sprites.get(0).setDead(true);
                        s.setDead(true);
                    }
                    break;

                case "playerbullet" :
                    s.moveUp();

                    sprites.stream().filter(e -> e.getType().equals("enemy")).forEach(enemy -> {
                        if(s.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                            enemy.setDead(true);
                            s.setDead(true);
                        }
                    });
                    break;
            }
        });
        sprites.stream().filter(s -> s.getType() == "enemy").filter(s -> time > 2).forEach(s -> {
            double probability;
            if ((sprites.get(0).getLayoutX() > s.getLayoutX() - 80) && (sprites.get(0).getLayoutX() < s.getLayoutX() + 80)) {
                probability = 1;
            } else {
                probability = 0.1;
            }
                if (Math.random() < probability) {
                    createBullet(s);
                }

        });
        for (Iterator<Sprite> iterator = sprites.iterator(); iterator.hasNext();) {
            Sprite sprite = iterator.next();
            if(sprite.getDead() == true) {
                GamePane.getChildren().remove(sprite);
                iterator.remove();
            }
        }

        for (Iterator<Sprite> iterator = bullets.iterator(); iterator.hasNext();) {
            Sprite bullet = iterator.next();
            if (bullet.getDead() == true || (bullet.getLayoutY() < -20 && bullet.getLayoutY() > GAME_HEIGHT +20)) {
                GamePane.getChildren().remove(bullet);
                iterator.remove();
            }
        }

        if (time > 2) {
            time = 0;
        }
    }

    private void endingGame() {

    }
}

