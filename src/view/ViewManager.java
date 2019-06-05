package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.*;
import org.apache.log4j.Logger;

import javax.swing.text.View;
import java.io.*;
import java.util.*;


public class ViewManager {

    private Logger logger = Logger.getLogger(ViewManager.class);

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;

    private final static int MENU_BUTTONS_START_X = 100;
    private final static int MENU_BUTTONS_START_Y = 150;

    private SpaceSubscene creditsSubscene;
    private SpaceSubscene helpSubscene;
    private SpaceSubscene scoreSubscene;
    private SpaceSubscene shipChooserSubscene;
    private SpaceSubscene gameOverSubscene;
    private SpaceSubscene addingScoreSubscene;

    private SpaceSubscene sceneToHide;

    private int score = 0;

    List<SpaceButton> menuButtons;

    List<ShipPicker> shipsList;
    private SHIP choosenShip;


    public ViewManager() {
        menuButtons = new ArrayList<>();
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        createSubscene();
        createButtons();
        createBackground();
        createLogo();
        createGameOver();
        createAddingScore();
        logger.info("View Manager created");


    }

    public Stage getMainStage() {
        return mainStage;
    }

    private void createSubscene() {
        creditsSubscene = new SpaceSubscene();
        mainPane.getChildren().add(creditsSubscene);
        InfoLabel authorLabel1 = new InfoLabel("AUTHOR");
        InfoLabel authorLabel2 = new InfoLabel("TOMASZ ZALUSKA");
        authorLabel1.setLayoutX(100);
        authorLabel1.setLayoutY(100);
        authorLabel2.setLayoutY(160);
        authorLabel2.setLayoutX(100);
        creditsSubscene.getPane().getChildren().add(authorLabel1);
        creditsSubscene.getPane().getChildren().add(authorLabel2);
        SpaceButton okButton = new SpaceButton("OK");
        creditsSubscene.getPane().getChildren().add(okButton);
        okButton.setLayoutX(200);
        okButton.setLayoutY(250);
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubscene(shipChooserSubscene);
            }
        });

        helpSubscene = new SpaceSubscene();
        mainPane.getChildren().add(helpSubscene);
        InfoLabel helpLabel1 = new InfoLabel("MOVE: A-left   D-right");
        InfoLabel helpLabel2 = new InfoLabel("SHOOT: SPACEBAR");
        helpSubscene.getPane().getChildren().addAll(helpLabel1, helpLabel2);
        helpLabel1.setLayoutX(100);
        helpLabel1.setLayoutY(100);
        helpLabel2.setLayoutX(100);
        helpLabel2.setLayoutY(160);

        scoreSubscene = new SpaceSubscene();
        mainPane.getChildren().add(scoreSubscene);

        createShipChooserSubscene();
    }

    private void createShipChooserSubscene() {
        shipChooserSubscene = new SpaceSubscene();
        mainPane.getChildren().add(shipChooserSubscene);

        InfoLabel chooseShipLabel = new InfoLabel("CHOOSE YOUR SHIP");
        chooseShipLabel.setLayoutX(110);
        chooseShipLabel.setLayoutY(25);
        shipChooserSubscene.getPane().getChildren().add(chooseShipLabel);
        shipChooserSubscene.getPane().getChildren().add(createShipToChoose());
        shipChooserSubscene.getPane().getChildren().add(createStartGameButton());
        logger.info("ShipChooser Subscene is created");


    }

    private HBox createShipToChoose() {
        HBox box = new HBox();
        box.setSpacing(20);shipsList = new ArrayList<>();
        for(SHIP ship : SHIP.values()) {
            ShipPicker shipToPick = new ShipPicker(ship);
            shipsList.add(shipToPick);
            box.getChildren().add(shipToPick);
            shipToPick.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    for(ShipPicker ship : shipsList) {
                        ship.setIsCircleChoosen(false);
                    }
                    shipToPick.setIsCircleChoosen(true);
                    choosenShip = shipToPick.getShip();
                }
            });
        }
        box.setLayoutX(300 - (118*2));
        box.setLayoutY(100);
        return box;
    }

    private SpaceButton createStartGameButton() {
        SpaceButton startButton = new SpaceButton("START");
        startButton.setLayoutX(350);
        startButton.setLayoutY(300);
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(choosenShip != null) {
                    GameViewManager gameViewManager = new GameViewManager();
                    gameViewManager.createNewGame(mainStage, choosenShip);
                    if (gameViewManager.getLose()){
                        showSubscene(gameOverSubscene);
                    } else {
                        showSubscene(addingScoreSubscene);
                        score = gameViewManager.getScore();
                    }
                }
            }
        });
        return startButton;
    }

    private void showSubscene(SpaceSubscene subscene) {
        if(sceneToHide != null) {
            sceneToHide.moveSubscene();
        }

        subscene.moveSubscene();
        sceneToHide = subscene;
    }

    private void addMenuButton(SpaceButton button) {
        button.setLayoutX(MENU_BUTTONS_START_X);
        button.setLayoutY(MENU_BUTTONS_START_Y + menuButtons.size() * 100);
        menuButtons.add(button);
        mainPane.getChildren().add(button);
    }

    private void createButtons() {
        createStartButton();
        createScoreButton();
        createHelpButton();
        createCreditsButton();
        createExitButton();
        logger.info("Buttons are created");
    }

    private void createStartButton() {
        SpaceButton startButton = new SpaceButton("PLAY");
        addMenuButton(startButton);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubscene(shipChooserSubscene);
            }
        });
    }

    private void createScoreButton() {
        SpaceButton scoreButton = new SpaceButton("SCORES");
        addMenuButton(scoreButton);

        scoreButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ProcessBuilder processBuilder = new ProcessBuilder("Notepad.exe", "src/scores.txt");
                try {
                    processBuilder.start();
                } catch (IOException e) {
                    System.err.println("File error");
                }
            }
        });
    }

    private void createHelpButton() {
        SpaceButton helpButton = new SpaceButton("HELP");
        addMenuButton(helpButton);

        helpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubscene(helpSubscene);
            }
        });
    }

    private void createCreditsButton() {
        SpaceButton creditsButton = new SpaceButton("CREDITS");
        addMenuButton(creditsButton);

        creditsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubscene(creditsSubscene);
            }
        });
    }

    private void createExitButton() {
        SpaceButton exitButton = new SpaceButton("EXIT");
        addMenuButton(exitButton);

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mainStage.close();
            }
        });
    }

    private void createBackground() {
        Image backgroundImage = new Image("view/resources/purple.png", 256, 256, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        mainPane.setBackground(new Background(background));
        logger.info("Background is created");
    }

    private void createLogo() {
        ImageView logo = new ImageView("view/resources/space_invaders.png");
        logo.setLayoutX(500);
        logo.setLayoutY(-20);

        mainPane.getChildren().add(logo);
        logger.info("Logo is created");
    }

    private void createGameOver() {
        gameOverSubscene = new SpaceSubscene();
        mainPane.getChildren().add(gameOverSubscene);
        InfoLabel gameoverLabel = new InfoLabel("GAME OVER");
        gameOverSubscene.getPane().getChildren().add(gameoverLabel);
        gameoverLabel.setLayoutX(110);
        gameoverLabel.setLayoutY(80);
        SpaceButton gameoverButton = new SpaceButton("OK");
        gameOverSubscene.getPane().getChildren().add(gameoverButton);
        gameoverButton.setLayoutX(200);
        gameoverButton.setLayoutY(180);
        gameoverButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubscene(shipChooserSubscene);
            }
        });
        logger.info("Gameover Subscene is created");
    }

    private void createAddingScore() {
        addingScoreSubscene = new SpaceSubscene();
        mainPane.getChildren().add(addingScoreSubscene);
        TextField textField = new TextField();
        HBox hBox = new HBox();
        hBox.getChildren().add(textField);
        textField.setPrefWidth(250);
        textField.setPrefHeight(50);
        try {
            textField.setFont(Font.loadFont(new FileInputStream(new File("src/model/resources/kenvector_future.ttf")), 30));
        } catch (FileNotFoundException e) {
            textField.setFont(Font.font("Verdana", 15));
        }
        hBox.setSpacing(20);
        addingScoreSubscene.getPane().getChildren().add(hBox);
        hBox.setLayoutX(180);
        hBox.setLayoutY(180);
        InfoLabel nameLabel = new InfoLabel("ENTER YOUR NAME: ");
        nameLabel.setLayoutX(100);
        nameLabel.setLayoutY(100);
        addingScoreSubscene.getPane().getChildren().add(nameLabel);
        SpaceButton okButton = new SpaceButton("OK");
        SpaceButton cancelButton = new SpaceButton("CANCEL");
        addingScoreSubscene.getPane().getChildren().addAll(okButton, cancelButton);
        okButton.setLayoutY(250);
        okButton.setLayoutX(330);
        cancelButton.setLayoutY(250);
        cancelButton.setLayoutX(80);
        final String[] name = new String[1];
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                name[0] = textField.getText();
                addScoreToFile(score, name[0]);
                showSubscene(shipChooserSubscene);
            }
        });
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubscene(shipChooserSubscene);
            }
        });
        logger.info("AddingScore Subscene is created");

    }


    private void addScoreToFile(int score, String name) {
        try {
            FileWriter file = new FileWriter("src/scores.txt", true);
            file.write(name + " " + score + "\r\n");
            file.close();
            logger.info("Adding score " + score + " " + name);
        } catch (FileNotFoundException e) {
            System.err.println("File does not exist");
        } catch (IOException e) {
            System.err.println("File Error");
        }

    }


}
