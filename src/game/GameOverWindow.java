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
    private final Button restartBtn;
    private final Label pointsLabel;
    private final Label overLabel;

    public GameOverWindow(Game game) {
        this.gameOverPane = new Pane();
        this.gameOverPane.setVisible(false);
        this.gameOverPane.setMaxSize(Game.WIDTH/2.0, Game.HEIGHT/2.5);
        this.gameOverPane.getStyleClass().add("over-pane");
        this.gameOverPane.setTranslateY(350);

        this.overLabel = new Label("Damn u ded.");
        this.overLabel.setTextFill(Color.AZURE);
        this.overLabel.setFont(Font.font("Helvetica", 44));

        this.pointsLabel = new Label("You got: ");
        this.pointsLabel.setTextFill(Color.AZURE);
        this.pointsLabel.setFont(Font.font("Helvetica", 44));

        this.restartBtn = new Button("Start over.");
        this.restartBtn.setOnAction(event -> game.restart());
        this.restartBtn.setPrefSize(200, 40);
        this.restartBtn.getStyleClass().add("btn-restart");

        VBox vBox = new VBox(this.overLabel, pointsLabel, this.restartBtn);
        vBox.setPrefSize(Game.WIDTH/2.0, Game.HEIGHT/2.5);
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);

        this.gameOverPane.getChildren().add(vBox);
    }

    public Pane getView() {
        return gameOverPane;
    }

    public void display(int pointsNum) {
        if (pointsNum > 49) this.overLabel.setText("Not gut...");
        if (pointsNum > 99) this.overLabel.setText("RAGE QUIT!!!");
        this.pointsLabel.setText("You got: " + pointsNum + " points.");
        this.gameOverPane.setVisible(true);
        this.restartBtn.setDisable(false);
        TranslateTransition open = new TranslateTransition(new Duration(350), this.gameOverPane);
        open.setToY(0);
        open.play();
        open.setOnFinished(event -> this.restartBtn.setDefaultButton(true));
    }

    public void hide() {
        this.restartBtn.setDisable(true);
        this.restartBtn.setDefaultButton(false);
        TranslateTransition close = new TranslateTransition(new Duration(350), this.gameOverPane);
        close.setToY(350);
        close.play();
        close.setOnFinished(event -> this.gameOverPane.setVisible(false));
    }
}
