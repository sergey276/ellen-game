package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.behaviours.Behaviour;

public class Alien extends AbstractActor implements Movable, Enemy, Alive {

    private final int speed = 50;
    private final Health health;
    private final Behaviour<? super Alien> behaviour;

    public Alien() {
        this(null, 100);
    }

    public Alien(int hp) {
        this(null, hp);
    }

    public Alien(Behaviour<? super Alien> behaviour) {
        this(behaviour, 100);
    }

    // головний конструктор
    public Alien(Behaviour<? super Alien> behaviour, int hp) {
        super("alien");
        this.behaviour = behaviour;
        this.health = new Health(hp);

        this.health.onFatigued(() -> {
            Scene scene = getScene();
            if (scene != null) {
                scene.removeActor(this);
            }
        });

        Animation animation = new Animation(
            "sprites/alien.png",
            32, 32,
            0.1f,
            Animation.PlayMode.LOOP_PINGPONG
        );
        setAnimation(animation);
    }

    // конструктор з параметрами (hp, behaviour), який вимагають тести
    public Alien(int hp, Behaviour<? super Alien> behaviour) {
        this(behaviour, hp);
    }

    @Override
    public Health getHealth() {
        return health;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public void startedMoving(Direction direction) {
        if (direction != Direction.NONE) {
            getAnimation().setRotation(direction.getAngle());
        }
    }

    @Override
    public void stoppedMoving() {
        // нічого робити не треба
    }

    private void doAttack() {
        Scene scene = getScene();
        if (scene == null) {
            return;
        }

        for (var actor : scene.getActors()) {
            if (actor instanceof Alive
                && !(actor instanceof Enemy)
                && this.intersects(actor)
            ) {
                ((Alive) actor).getHealth().drain(1);
            }
        }
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);

        scene.cancelActions(this);

        if (behaviour != null) {
            behaviour.setUp(this);
        }

        new sk.tuke.kpi.gamelib.framework.actions.Loop<>(
            new Invoke<>(this::doAttack)
        ).scheduleFor(this);
    }
}
