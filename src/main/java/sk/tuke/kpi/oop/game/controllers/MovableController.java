package sk.tuke.kpi.oop.game.controllers;

import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.actions.Move;

import java.util.*;

public class MovableController implements KeyboardListener {
    private final Map<Input.Key, Direction> keyDirectionMap = Map.ofEntries(
        Map.entry(Input.Key.UP, Direction.NORTH),
        Map.entry(Input.Key.DOWN, Direction.SOUTH),
        Map.entry(Input.Key.LEFT, Direction.WEST),
        Map.entry(Input.Key.RIGHT, Direction.EAST)
    );
    private final Movable actor;
    private Move<Movable> moveAction;
    private final Set<Input.Key> pressedKeys = new LinkedHashSet<>();

    public MovableController(Movable actor) {
        this.actor = actor;
    }


    @Override
    public void keyPressed(Input.Key key) {
        if (!keyDirectionMap.containsKey(key)) return;
        pressedKeys.add(key);
        updateMovement();
    }

    @Override
    public void keyReleased(Input.Key key) {
        if (!keyDirectionMap.containsKey(key)) return;
        pressedKeys.remove(key);
        updateMovement();
    }

    private void updateMovement() {
        if (pressedKeys.isEmpty()) {
            if (moveAction != null) moveAction.stop();
            return;
        }

        Direction combinedDirection = Direction.NONE;
        for (Input.Key key : pressedKeys) {
            Direction dir = keyDirectionMap.get(key);
            combinedDirection = Direction.combine(combinedDirection, dir);
        }

        if (moveAction != null) moveAction.stop();
        moveAction = new Move<>(combinedDirection, Float.MAX_VALUE);
        moveAction.scheduleFor(actor);
    }
}
