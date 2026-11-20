package sk.tuke.kpi.oop.game.behaviours;

import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.actions.Move;
import sk.tuke.kpi.oop.game.Direction;

import java.util.Random;

public class RandomlyMoving implements Behaviour<Movable> {

    private final Random random = new Random();

    @Override
    public void setUp(Movable actor) {
        if (actor == null || actor.getScene() == null) return;
        startMoving(actor);
    }

    private void startMoving(Movable actor) {
        Direction[] dirs = {
            Direction.NORTH, Direction.SOUTH,
            Direction.EAST,  Direction.WEST,
            Direction.NORTH_EAST, Direction.NORTH_WEST,
            Direction.SOUTH_EAST, Direction.SOUTH_WEST
        };

        Direction dir = dirs[random.nextInt(dirs.length)];
        float duration = 1f + random.nextFloat() * 2f;  

        Move<Movable> move = new Move<>(dir, duration);
        move.scheduleFor(actor);

        
        new Invoke<Movable>(() -> checkMove(actor, move)).scheduleFor(actor);
    }

    private void checkMove(Movable actor, Move<Movable> move) {
        if (move.isDone()) {
            startMoving(actor);
        } else {
            
            new Invoke<Movable>(() -> checkMove(actor, move)).scheduleFor(actor);
        }
    }
}
