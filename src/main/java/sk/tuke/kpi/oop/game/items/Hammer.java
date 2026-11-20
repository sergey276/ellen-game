package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Repairable;

public class Hammer extends BreakableTool<Repairable>
    implements Collectible, Usable<Repairable> {

    public Hammer() {
        super(1);
        setAnimation(new Animation("sprites/hammer.png", 16, 16));
    }

    @Override
    public void useWith(Repairable target) {
        if (target == null) return;

        target.repair();          // виконуємо ремонт
        super.useWith(target);    // зменшуємо durability інструмента
    }

    @Override
    public Class<Repairable> getUsingActorClass() {
        return Repairable.class;
    }
}
