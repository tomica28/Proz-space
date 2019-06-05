package model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GameInfoLabel extends Label {
    private Logger logger = Logger.getLogger(GameInfoLabel.class);
    private final static String FONT_PATH = "src/model/resources/kenvector_future.ttf";

    public GameInfoLabel(String text) {
        setPrefWidth(130);
        setPrefHeight(50);
        BackgroundImage backgroundImage = new BackgroundImage(new Image("view/resources/yellow_info.png", 130, 50, false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        setBackground(new Background(backgroundImage));
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(10, 10, 10, 10));
        setLabelFont();
        setText(text);
        logger.info("Creating GameInfoLabel with text: " + text);
    }

    private void setLabelFont() {
        try {
            logger.info("GameInfoLabel setting space font");
            setFont(Font.loadFont(new FileInputStream(new File(FONT_PATH)), 15));
        } catch (FileNotFoundException e) {
            logger.info("GameInfoLabel setting Verdana font");
            setFont(Font.font("Verdana", 15));
        }
    }
}
