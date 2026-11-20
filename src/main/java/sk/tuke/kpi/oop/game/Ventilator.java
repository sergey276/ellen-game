package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.messages.Topic;

public class Ventilator extends AbstractActor implements Repairable {

    public static final Topic<Ventilator> VENTILATOR_REPAIRED =
        Topic.create("VENTILATOR_REPAIRED", Ventilator.class);

    private Animation animation;

    public Ventilator() {
        animation = new Animation("sprites/ventilator.png", 32, 32, 0.1f);
        setAnimation(animation);
        animation.stop();
    }

    private boolean broken = true;

    @Override
    public void repair() {
        broken = false;
        animation.play();
    }

    public boolean isBroken() {
        return broken;
    }
}
