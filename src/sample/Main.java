package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import view.ViewManager;

public class Main extends Application {

    public static Logger logger = Logger.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            ViewManager manager = new ViewManager();
            primaryStage = manager.getMainStage();
            primaryStage.show();
            logger.info("Application started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        DOMConfigurator.configure("log4j2.xml");
        launch(args);
    }
}
