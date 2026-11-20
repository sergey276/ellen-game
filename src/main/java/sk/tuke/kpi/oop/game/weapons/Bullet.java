package sk.tuke.kpi.oop.game.weapons;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.characters.Alive;

public class Bullet extends AbstractActor implements Fireable, Movable {

    private final int speed = 200;

    public Bullet() {
        super("bullet");

        Animation animation = new Animation(
            "sprites/bullet.png",
            16, 16,
            0.05f,
            Animation.PlayMode.LOOP
        );

        setAnimation(animation);
    }

    @Override
    public void startedMoving(Direction direction) {
        getAnimation().setRotation(direction.getAngle());
    }

    @Override
    public void stoppedMoving() {
        // нічого
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public void collidedWithWall() {
        Scene scene = getScene();
        if (scene != null) {
            scene.removeActor(this);
        }
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);

        new sk.tuke.kpi.gamelib.framework.actions.Loop<>(
            new Invoke<>(this::checkCollisions)
        ).scheduleFor(this);
    }

    public void checkCollisions() {
        Scene scene = getScene();
        if (scene == null) {
            return;
        }

        for (Actor actor : scene.getActors()) {
            if (actor instanceof Alive
                && actor != this
                && this.intersects(actor)
            ) {
                ((Alive) actor).getHealth().drain(15);
                scene.removeActor(this);
                return;
            }
        }
    }
}
