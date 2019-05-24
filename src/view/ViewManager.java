package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.SpaceButton;
import model.SpaceSubscene;

import java.util.ArrayList;
import java.util.List;


public class ViewManager {

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

    private SpaceSubscene sceneToHide;

    List<SpaceButton> menuButtons;

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


    }

    public Stage getMainStage() {
        return mainStage;
    }

    private void createSubscene() {
        creditsSubscene = new SpaceSubscene();
        mainPane.getChildren().add(creditsSubscene);

        helpSubscene = new SpaceSubscene();
        mainPane.getChildren().add(helpSubscene);

        scoreSubscene = new SpaceSubscene();
        mainPane.getChildren().add(scoreSubscene);

        shipChooserSubscene = new SpaceSubscene();
        mainPane.getChildren().add(shipChooserSubscene);
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
                showSubscene(creditsSubscene);
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
    }

    private void createLogo() {
        ImageView logo = new ImageView("view/resources/space_invaders.png");
        logo.setLayoutX(500);
        logo.setLayoutY(-20);

        mainPane.getChildren().add(logo);
    }


}
