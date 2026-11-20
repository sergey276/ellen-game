package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Direction;
import sk.tuke.kpi.oop.game.characters.Armed;
import sk.tuke.kpi.oop.game.weapons.Fireable;

public class Fire<A extends Actor & Armed> extends AbstractAction<A> {

    @Override
    public void execute(float deltaTime) {
        if (isDone()) {
            return;
        }

        A actor = getActor();
        if (actor == null) {
            setDone(true);
            return;
        }

        if (actor.getFirearm() == null) {
            setDone(true);
            return;
        }

        Fireable fireable = actor.getFirearm().fire();
        if (fireable == null) {
            // немає набоїв – нічого не робимо, але дія завершена
            setDone(true);
            return;
        }

        if (actor.getScene() != null) {
            // додаємо fireable у сцену
            int x = actor.getPosX();
            int y = actor.getPosY();
            actor.getScene().addActor(fireable, x, y);

            // визначаємо напрямок за ротацією анімації актора
            Direction direction = Direction.fromAngle(
                actor.getAnimation() != null ? actor.getAnimation().getRotation() : 0f
            );

            // плануємо рух для fireable
            new Move<Fireable>(direction, Float.MAX_VALUE).scheduleFor(fireable);
        }

        setDone(true);
    }
}
