package game;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class GameOverWindow {

    private final Pane gameOverPane;

    public GameOverWindow(Game game) {
        this.gameOverPane = new Pane();
        this.gameOverPane.setVisible(false);
        this.gameOverPane.setMaxSize(Game.WIDTH/3.0, Game.HEIGHT/3.0);
        this.gameOverPane.getStyleClass().add("over-pane");
        this.gameOverPane.setTranslateY(350);

        Label overText = new Label("Not gut...");
        overText.setTextFill(Color.AZURE);
        overText.setFont(Font.font("Helvetica", 44));

        Button button = new Button("Start over.");
        button.setOnMouseClicked(mouseEvent -> game.restart());
        button.setPrefSize(200, 40);
        button.getStyleClass().add("btn-restart");

        VBox vBox = new VBox(overText, button);
        vBox.setPrefSize(Game.WIDTH/3.0, Game.HEIGHT/3.0);
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);

        this.gameOverPane.getChildren().add(vBox);
    }

    public Pane getView() {
        return gameOverPane;
    }

    public void display() {
        // TODO make gameOverWindow slide in\out from the bottom smoothly
        this.gameOverPane.setVisible(true);
        TranslateTransition open = new TranslateTransition(new Duration(350), this.gameOverPane);
        open.setToY(0);
        open.play();
    }

    public void hide() {
        TranslateTransition close = new TranslateTransition(new Duration(350), this.gameOverPane);
        close.setToY(350);
        close.play();
        close.setOnFinished(event -> this.gameOverPane.setVisible(false));
    }
}
