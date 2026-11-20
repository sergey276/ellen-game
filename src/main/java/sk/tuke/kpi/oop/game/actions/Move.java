package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.actions.Action;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;

public class Move<A extends Movable> implements Action<A> {
    private A actor;
    private final Direction direction;
    private final float duration;
    private float elapsed;
    private boolean started;
    private boolean done;
    private float fx, fy;   

    public Move(Direction direction, float duration) {
        this.direction = direction;
        this.duration = Math.max(0f, duration);
    }

    public Move(Direction direction) {
        this(direction, 0f);
    }

    @Override
    public void setActor(A actor) {
        this.actor = actor;
        if (actor != null) {
            fx = actor.getPosX();
            fy = actor.getPosY();
        }
    }

    @Override
    public A getActor() {
        return actor;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public void reset() {
        elapsed = 0f;
        started = false;
        done = false;
    }

    public void stop() {
        if (done) return;
        done = true;
        if (actor != null) actor.stoppedMoving();
    }

    @Override
    public void execute(float deltaTime) {
        if (actor == null || done || direction == null) return;

        if (!started) {
            started = true;
            actor.startedMoving(direction);
        }

        float speed = actor.getSpeed();

        float newX = fx + direction.getDx() * speed * deltaTime;
        float newY = fy + direction.getDy() * speed * deltaTime;

        
        if (actor.getScene() != null) {
            actor.setPosition(Math.round(newX), Math.round(newY));
            if (actor.getScene().getMap().intersectsWithWall(actor)) {
                
                actor.setPosition(Math.round(fx), Math.round(fy));
                actor.collidedWithWall();
                stop();
                return;
            }
        }

        
        fx = newX;
        fy = newY;
        actor.setPosition(Math.round(fx), Math.round(fy));

        elapsed += deltaTime;

        
        if (duration == 0f || elapsed >= duration) {
            stop();
        }
    }
}
