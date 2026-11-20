package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.items.BreakableTool;
import sk.tuke.kpi.oop.game.items.Usable;

public class Use<A extends Actor> extends AbstractAction<A> {

    private final Usable<A> usable;

    public Use(Usable<A> usable) {
        this.usable = usable;
    }

    @Override
    public void execute(float deltaTime) {
        if (isDone()) {
            return;
        }

        A actor = getActor();

        // якщо актор не призначений, але execute уже викликали —
        // дія все одно повинна вважатись завершеною
        if (actor == null) {
            setDone(true);
            return;
        }

        usable.useWith(actor);
        handleBreakableTool();

        setDone(true);
    }

    private void handleBreakableTool() {
        if (!(usable instanceof BreakableTool<?>)) {
            return;
        }

        BreakableTool<?> tool = (BreakableTool<?>) usable;

        if (tool.getRemainingUses() > 0) {
            return;
        }

        Actor owner = tool.getOwner();
        if (owner instanceof Keeper) {
            new Drop<Keeper>().scheduleFor((Keeper) owner);
        }
    }

    public Disposable scheduleForIntersectingWith(Actor mediatingActor) {
        Scene scene = mediatingActor.getScene();
        if (scene == null) {
            return null;
        }

        Class<A> usingActorClass = usable.getUsingActorClass();

        for (Actor actor : scene) {
            if (mediatingActor.intersects(actor) && usingActorClass.isInstance(actor)) {
                return this.scheduleFor(usingActorClass.cast(actor));
            }
        }
        return null;
    }
}
