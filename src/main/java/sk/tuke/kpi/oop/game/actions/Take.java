package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.items.Ammo;
import sk.tuke.kpi.oop.game.items.Collectible;
import sk.tuke.kpi.oop.game.items.Energy;
import sk.tuke.kpi.oop.game.characters.Ripley;

public class Take<A extends Keeper> extends AbstractAction<A> {

    @Override
    public void execute(float deltaTime) {
        A keeper = getActor();
        if (keeper == null || keeper.getScene() == null) {
            setDone(true);
            return;
        }

        Scene scene = keeper.getScene();

        for (Actor actor : scene.getActors()) {

            if (!(actor instanceof Collectible)) continue;
            if (!actor.intersects(keeper)) continue;

            if (actor instanceof Energy) {
                ((Energy) actor).useWith((Ripley) keeper);
                setDone(true);
                return;
            }

            if (actor instanceof Ammo) {
                ((Ammo) actor).useWith((Ripley) keeper);
                setDone(true);
                return;
            }

            try {
                keeper.getBackpack().add((Collectible) actor);
                scene.removeActor(actor);
            } catch (IllegalStateException ignored) {}

            break;
        }

        setDone(true);
    }
}
