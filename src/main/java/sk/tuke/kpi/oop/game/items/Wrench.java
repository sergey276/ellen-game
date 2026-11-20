package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.DefectiveLight;
import sk.tuke.kpi.oop.game.Repairable;

public class Wrench extends BreakableTool<Repairable>
    implements Collectible, Usable<Repairable> {

    public Wrench() {
        super(2);
        setAnimation(new Animation("sprites/wrench.png", 16, 16));
    }

    @Override
    public void useWith(Repairable target) {
        if (target == null) return;

        target.repair();        
        super.useWith(target);  
    }

    @Override
    public Class<Repairable> getUsingActorClass() {
        return Repairable.class;
    }
}
