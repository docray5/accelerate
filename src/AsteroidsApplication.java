import game.GameLoop;
import javafx.application.Application;
import javafx.stage.Stage;


public class AsteroidsApplication extends Application {

    @Override
    public void start(Stage stage) {
        GameLoop gameLoop = new GameLoop();

        // try position this at the bottom
        gameLoop.start();

        stage.setTitle("Asteroids!");
        stage.setScene(gameLoop.getGame().getScene());
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(AsteroidsApplication.class);
    }
}