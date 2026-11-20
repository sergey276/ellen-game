package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.items.Hammer;
import sk.tuke.kpi.oop.game.items.Usable;
import sk.tuke.kpi.gamelib.Actor;

public class Locker extends AbstractActor implements Usable<Actor> {

    private boolean opened = false;

    public Locker() {
        setAnimation(new Animation("sprites/locker.png", 16, 16));
    }

    @Override
    public void useWith(Actor actor) {
        if (opened) return;
        opened = true;

        Hammer hammer = new Hammer();

        if (getScene() != null) {
            getScene().addActor(hammer, getPosX(), getPosY());
        }
    }

    @Override
    public Class<Actor> getUsingActorClass() {
        return Actor.class;
    }
}
