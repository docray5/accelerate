/*
---------------------------------------------------------
This Abstract class is WIP and will probably get removed,
but I might use it for a bit of cleaning as Projectiles,
Asteroids and Particles classes all share a lot of code
---------------------------------------------------------
 */
// TODO use it cuz it might have some benefits

import javafx.scene.layout.Pane;

import java.util.ArrayList;

public abstract class Characters {
    private final ArrayList<Character> characters;
    private final Pane pane;

    public Characters(Pane pane) {
        this.characters = new ArrayList<>();
        this.pane = pane;
    }

    public void move() {
        this.characters.forEach(Character::move);
    }

    public void checkLifeTime(long now) {
        for (Character character : this.characters) {
            if (now - character.getTimeBorn() > character.getLifeTime()) character.setAlive(false);
        }
    }

    public ArrayList<Character> getList() {
        return characters;
    }

    public Pane getPane() {
        return pane;
    }

    public void removeIfNotAlive() {
        this.characters.stream()
                .filter(Character::isNotAlive)
                .forEach(character -> pane.getChildren().remove(character.getCharacter()));
        this.characters.removeAll(this.characters.stream()
                .filter(Character::isNotAlive)
                .toList());
    }
}
