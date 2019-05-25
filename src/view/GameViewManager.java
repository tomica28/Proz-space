package view;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.SHIP;
import javafx.scene.image.ImageView;

public class GameViewManager {
    private AnchorPane GamePane;
    private Stage GameStage;
    private Scene GameScene;

    private static final int GAME_WIDTH = 600;
    private static final int GAME_HEIGHT = 800;

    private Stage menuStage;
    private ImageView ship;

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private AnimationTimer gameTimer;

    private GridPane gridPane1;
    private GridPane gridPane2;
    private final static String BACKGROUND_IMAGE = "view/resources/purple.png";

    public GameViewManager() {
        initializeStage();
        createKeyListeners();
    }

    private void initializeStage() {
        GamePane = new AnchorPane();
        GameScene = new Scene(GamePane, GAME_WIDTH, GAME_HEIGHT );
        GameStage = new Stage();
        GameStage.setScene(GameScene);
    }

    private void createKeyListeners() {
        GameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.A) {
                    isLeftKeyPressed = true;
                } else if(keyEvent.getCode() == KeyCode.D) {
                    isRightKeyPressed = true;
                }
            }
        });

        GameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.A) {
                    isLeftKeyPressed = false;
                } else if(keyEvent.getCode() == KeyCode.D) {
                    isRightKeyPressed = false;
                }
            }
        });
    }

    public void createNewGame(Stage menuStage, SHIP choosenShip) {
        this.menuStage = menuStage;
        this.menuStage.hide();
        createBackground();
        createShip(choosenShip);
        createGameLoop();
        GameStage.show();
    }

    private void createShip(SHIP choosenShip) {
        ship = new ImageView(choosenShip.getUrl());
        ship.setLayoutX(GAME_WIDTH/2);
        ship.setLayoutY(GAME_HEIGHT-90);
        GamePane.getChildren().add(ship);
    }

    private void createGameLoop() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                moveBackground();
                moveShip();
            }
        };
        gameTimer.start();
    }

    private void moveShip() {
        if(isLeftKeyPressed && !isRightKeyPressed) {
            if(ship.getLayoutX() > 0) {
                ship.setLayoutX(ship.getLayoutX() - 3);
            }
        }
        if(!isLeftKeyPressed && isRightKeyPressed) {
            if(ship.getLayoutX() < 500) {
                ship.setLayoutX(ship.getLayoutX() +3);
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
}

