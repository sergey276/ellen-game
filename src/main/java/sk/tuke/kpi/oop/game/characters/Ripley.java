package sk.tuke.kpi.oop.game.characters;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.Movable;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.items.Backpack;
import sk.tuke.kpi.oop.game.Ventilator;
import sk.tuke.kpi.oop.game.weapons.Firearm;
import sk.tuke.kpi.oop.game.weapons.Gun;

public class Ripley extends AbstractActor implements Movable, Keeper, Alive, Armed, Actor {

    public static final Topic<Ripley> RIPLEY_DIED =
        Topic.create("RIPLEY_DIED", Ripley.class);

    private int speed = 100;

    private final Backpack backpack;
    private final Animation animation;

    private Health health;
    private Firearm firearm;

    public Ripley() {
        super("Ripley");

        this.backpack = new Backpack("Ripley's backpack", 10);

        this.animation = new Animation(
            "sprites/player.png",
            32, 32,
            0.1f,
            Animation.PlayMode.LOOP_PINGPONG
        );
        setAnimation(animation);

        this.health = new Health(100);

        health.onFatigued(() -> {
            System.out.println("RIPLEY HAS 0 HEALTH — publishing death event");

            Animation death = new Animation(
                "sprites/player_die.png",
                32, 32,
                0.1f,
                Animation.PlayMode.ONCE
            );
            setAnimation(death);

            if (getScene() != null) {
                getScene().cancelActions(this);
                getScene().getMessageBus().publish(RIPLEY_DIED, this);
            }
        });

        this.firearm = new Gun(2000, 2000);
    }

    @Override
    public void startedMoving(Direction direction) {
        setAnimation(animation);
        getAnimation().setRotation(direction.getAngle());
    }

    @Override
    public void stoppedMoving() {}

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public Health getHealth() {
        return health;
    }

    @Override
    public Firearm getFirearm() {
        return firearm;
    }

    @Override
    public void setFirearm(Firearm weapon) {
        this.firearm = weapon;
    }

    @Override
    public Backpack getBackpack() {
        return backpack;
    }

    public void showRipleyState() {
        if (getScene() == null) return;
        var game = getScene().getGame();

        game.getOverlay().drawText(
            "Health: " + health.getValue() +
                "   Ammo: " + firearm.getAmmo(),
            130, 570
        );
    }

    public class EnergyDrainAction extends sk.tuke.kpi.gamelib.framework.actions.AbstractAction<Ripley> {

        private float timer = 0f;

        @Override
        public void execute(float deltaTime) {
            Ripley r = getActor();
            if (r == null) return;

            timer += deltaTime;

            if (timer >= 1f) {
                Ventilator v = r.getScene().getFirstActorByType(Ventilator.class);

                if (v != null && v.isBroken()) {
                    r.getHealth().drain(1);
                }

                timer = 0f;
            }
        }
    }
}
