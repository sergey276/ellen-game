package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.gamelib.Scene;

public abstract class BreakableTool<A extends Actor> extends AbstractActor implements Usable<A>, Collectible {

    private int remainingUses;
    private Keeper owner;
    private Actor target;

    public BreakableTool(int uses) {
        this.remainingUses = uses;
    }

    public void setOwner(Keeper owner) { this.owner = owner; }
    public Keeper getOwner() { return owner; }

    public void setTarget(Actor target) { this.target = target; }
    public Actor getTarget() { return target; }

    public int getRemainingUses() { return remainingUses; }

    @Override
    public void useWith(A actor) {

        if (remainingUses <= 0) return;

        remainingUses--;


        if (remainingUses == 0) {
            if (getScene() != null) getScene().removeActor(this);
        }
    }
}
