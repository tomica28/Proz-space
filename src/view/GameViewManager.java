package view;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.GameInfoLabel;
import model.SHIP;
import javafx.scene.image.ImageView;
import model.Sprite;
import org.apache.log4j.Logger;


import java.util.*;

public class GameViewManager {

    private Logger logger = Logger.getLogger(GameViewManager.class);

    private AnchorPane GamePane;
    private Stage GameStage;
    private Scene GameScene;

    private static boolean lose;

    private static final int GAME_WIDTH = 600;
    private static final int GAME_HEIGHT = 800;
    private double shootingFrequency = 0;
    private double shootingTime = 1;
    private int ENEMIES_IN_LANE = 4;

    private Timer timer;
    private TimerTask timerTask;
    private int secoundPassed = 0;


    private Stage menuStage;

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private boolean isShooting = false;
    private AnimationTimer gameTimer;

    private GridPane gridPane1;
    private GridPane gridPane2;
    private final static String BACKGROUND_IMAGE = "view/resources/purple.png";

    private GameInfoLabel pointsLabel;
    private ImageView[] playerLives;

    private final static String BLACK_ENEMY_IMAGE = "view/resources/enemyBlack.png";
    private final static String GREEN_ENEMY_IMAGE = "view/resources/enemyGreen.png";
    private final static String RED_ENEMY_IMAGE = "view/resources/enemyRed.png";
    private List<Sprite> sprites;
    private final static int ENEMY_XPOSITION = 40;
    private final static int ENEMY_YPOSITION = 90;
    private int liveEnemies = 0;

    private List<Sprite> bullets;
    private final static String BULLET_IMAGE = "view/resources/bullet.png";

    public GameViewManager() {
        initializeStage();
        createKeyListeners();
        logger.info("GameViewManager is created");
    }

    public boolean getLose() {
        logger.info("Game status lose: " + lose);
        return lose;
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
        logger.info("Key listeners are created");
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
        createGameLabel(choosenShip);
        createShip(choosenShip);
        createEnemies();
        createGameLoop();
        createTimer();
        GameStage.showAndWait();
        logger.info("New game is created");
    }

    private void createTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                secoundPassed++;
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);

    }

    private void createGameLabel(SHIP choosenShip) {
        pointsLabel = new GameInfoLabel("TIME : 00");
        pointsLabel.setLayoutX(460);
        pointsLabel.setLayoutY(10);
        GamePane.getChildren().add(pointsLabel);
        playerLives = new ImageView[3];

        for (int i = 0; i < playerLives.length; i++) {
            playerLives[i] = new ImageView(choosenShip.getUrlLife());
            playerLives[i].setLayoutX(455 + i*50);
            playerLives[i].setTranslateY(65);
            GamePane.getChildren().add(playerLives[i]);
        }
        logger.info("Game label is created");

    }

    private void createShip(SHIP choosenShip) {
        Sprite ship = new Sprite(choosenShip.getUrl(), "player");
        ship.setLives(3);
        sprites.add(ship);
        sprites.get(0).setLayoutX(GAME_WIDTH/2);
        sprites.get(0).setLayoutY(GAME_HEIGHT-90);
        GamePane.getChildren().add(sprites.get(0));
        logger.info("Ship is created");
        logger.info("Choosen ship: " + choosenShip.getUrl());
    }

    private void createGameLoop() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                checkingIfEnd();
                moveBackground();
                moveShip();
                shooting();
                checkingIfdead();
                moveEnemies();
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
        String textToSet = "TIME : ";
        pointsLabel.setText(textToSet + secoundPassed);
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
        for(int i = 0; i < ENEMIES_IN_LANE; i++) {
            Sprite enemy = new Sprite(BLACK_ENEMY_IMAGE, "enemy");
            GamePane.getChildren().add(enemy);
            enemy.setLayoutX(ENEMY_XPOSITION + 140*i);
            enemy.setLayoutY(ENEMY_YPOSITION);
            enemy.setLives(3);
            enemy.isMovingLeft = true;
            enemy.isMovingRight = false;
            enemy.setSpeed(1.5);
            sprites.add(enemy);
            liveEnemies++;
        }
        logger.info("First lane is created");

        //creating second lane of enemies
        for (int i = 0; i < ENEMIES_IN_LANE; i++) {
            Sprite enemy = new Sprite(GREEN_ENEMY_IMAGE, "enemy");
            GamePane.getChildren().add(enemy);
            enemy.setLayoutY(ENEMY_YPOSITION + 90);
            enemy.setLayoutX(ENEMY_XPOSITION + 30 + 140*i);
            enemy.setLives(2);
            enemy.isMovingRight = true;
            enemy.isMovingLeft = false;
            enemy.setSpeed(1.1);
            sprites.add(enemy);
            liveEnemies++;
        }
        logger.info("Second lane is created");

        //creating third lane of enemies
        for (int i = 0; i < ENEMIES_IN_LANE; i++) {
            Sprite enemy = new Sprite(RED_ENEMY_IMAGE, "enemy");
            GamePane.getChildren().add(enemy);
            enemy.setLayoutY(ENEMY_YPOSITION + 180);
            enemy.setLayoutX(ENEMY_XPOSITION + 140*i);
            enemy.setLives(1);
            enemy.isMovingRight = false;
            enemy.isMovingLeft = true;
            enemy.setSpeed(0.7);
            sprites.add(enemy);
            liveEnemies++;
        }
        logger.info("Third lane is created");
    }

    private void moveEnemies() {
        for (int i = 0; i < sprites.size() -1 ; i++) {
            Sprite sprite = sprites.get(i+1);
            if(sprite.isMovingLeft == true){
                sprite.moveLeft();
            }
            if(sprite.isMovingRight == true) {
                sprite.moveRight();
            }
        }
        for (int i = 0; i < sprites.size() -1; i++) {
            Sprite sprite = sprites.get(i+1);
            if(sprite.getLayoutX() > ENEMY_XPOSITION + (i%ENEMIES_IN_LANE)*140 +40) {
                sprite.isMovingLeft = true;
                sprite.isMovingRight = false;
            }

            if(sprite.getLayoutX() < ENEMY_XPOSITION + (i%ENEMIES_IN_LANE)*140 - 40) {
                sprite.isMovingRight = true;
                sprite.isMovingLeft = false;
            }
        }
    }

    private void createBullet(Sprite who) {
        Sprite bullet = new Sprite(BULLET_IMAGE, who.getType() + "bullet");
        bullet.setLayoutX(who.getLayoutX() + 45);
        if(who.getType().equals("player")) {
            bullet.setLayoutY(who.getLayoutY());
        } else if(who.getType().equals("enemy")) {
            bullet.setLayoutY(who.getLayoutY() + 70);
        }
        GamePane.getChildren().add(bullet);
        bullets.add(bullet);
    }

    private void shooting() {
        shootingFrequency += 0.016;
        shootingTime += 0.016;
        bullets.forEach(s -> {
            switch (s.getType()) {
                case "enemybullet":
                    s.moveDown();

                    if (s.getBoundsInParent().intersects(sprites.get(0).getBoundsInParent())) {
                        sprites.get(0).setLives(sprites.get(0).getLives() - 1);
                        GamePane.getChildren().remove(playerLives[sprites.get(0).getLives()]);
                        s.setDead(true);
                        logger.info("Player is hit");
                        logger.info("Player lives: " + sprites.get(0).getLives());
                    }
                    break;

                case "playerbullet" :
                    s.moveUp();

                    sprites.stream().filter(e -> e.getType().equals("enemy")).forEach(enemy -> {
                        if(s.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                            enemy.setLives(enemy.getLives() - 1);
                            s.setDead(true);
                            logger.info("Enemy is hit");
                        }
                    });
                    break;
            }
        });
        sprites.stream().filter(s -> s.getType() == "enemy").filter(s -> shootingFrequency > 2).forEach(s -> {
            double probability;
            if ((sprites.get(0).getLayoutX() > s.getLayoutX() - 80) && (sprites.get(0).getLayoutX() < s.getLayoutX() + 80)) {
                probability = 1;
            } else {
                probability = 0.1;
            }

            if((sprites.indexOf(s) + ENEMIES_IN_LANE <= sprites.size() -1 && sprites.get(sprites.indexOf(s) + ENEMIES_IN_LANE).getDead() != false) || sprites.indexOf(s) + ENEMIES_IN_LANE > sprites.size()-1) {
                if (Math.random() < probability) {
                    createBullet(s);
                }
            }

        });
        for (Iterator<Sprite> iterator = sprites.iterator(); iterator.hasNext();) {
            Sprite sprite = iterator.next();
            if(sprite.getDead() == true) {
                Sprite emptyEnemy = new Sprite(BLACK_ENEMY_IMAGE, "emptyenemy");
                emptyEnemy.setDead(false);
                emptyEnemy.setVisible(false);
                emptyEnemy.setLayoutX(1024);
                sprites.set(sprites.indexOf(sprite), emptyEnemy);
                GamePane.getChildren().remove(sprite);
            }
        }

        for (Iterator<Sprite> iterator = bullets.iterator(); iterator.hasNext();) {
            Sprite bullet = iterator.next();
            if (bullet.getDead() == true || (bullet.getLayoutY() < -20 && bullet.getLayoutY() > GAME_HEIGHT +20)) {
                GamePane.getChildren().remove(bullet);
                iterator.remove();
            }
        }

        if (shootingFrequency > 2) {
            shootingFrequency = 0;
        }
    }

    private void checkingIfdead() {
        for (Sprite sprite : sprites) {
            if (sprite.getLives() == 0) {
                sprite.setDead(true);
                if(sprite.getType().equals("enemy")) {
                    liveEnemies--;
                    logger.info("Enemy is dead");
                    logger.info("Amout of enemies: " + liveEnemies);
                }
            }
        }
    }

    private void checkingIfEnd() {
        if (!sprites.get(0).getType().equals("player")) {
            logger.info("Game over");
            lose = true;
            ending();
        }
        if(liveEnemies == 0) {
            logger.info("Win");
            lose = false;
            ending();
        }
    }

    private void ending() {
            timerTask.cancel();
            timer.cancel();
            GameStage.close();
            gameTimer.stop();
            menuStage.show();
    }

    public int getScore() {
        return secoundPassed;
    }

}

