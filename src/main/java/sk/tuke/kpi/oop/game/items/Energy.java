package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.characters.Alive;

public class Energy extends AbstractActor implements Collectible, Usable<Alive> {

    public Energy() {
        setAnimation(new Animation("sprites/energy.png", 16, 16));
    }

    @Override
    public void useWith(Alive actor) {
        if (actor == null) {
            return;
        }

        actor.getHealth().refill(50);

        if (getScene() != null) {
            getScene().removeActor(this);
        }
    }

    @Override
    public Class<Alive> getUsingActorClass() {
        return Alive.class;
    }
}
