package sk.tuke.kpi.oop.game.controllers;

import org.jetbrains.annotations.NotNull;
import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import sk.tuke.kpi.oop.game.characters.Armed;
import sk.tuke.kpi.oop.game.actions.Fire;

public class ShooterController implements KeyboardListener {

    private Armed actor;

    public ShooterController(Armed actor) {
        this.actor = actor;
    }

    @Override
    public void keyPressed(@NotNull Input.Key key) {
        if (key == Input.Key.SPACE && actor instanceof sk.tuke.kpi.gamelib.Actor) {
            new Fire<>().scheduleFor((sk.tuke.kpi.gamelib.Actor & Armed) actor);
        }
    }
}
