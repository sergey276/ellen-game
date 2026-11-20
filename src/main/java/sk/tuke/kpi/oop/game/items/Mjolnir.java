package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Repairable;

public class Mjolnir extends BreakableTool<Repairable>
    implements Collectible, Usable<Repairable> {

    public Mjolnir() {
        super(4);
        setAnimation(new Animation("sprites/Mjolnir.png", 16, 16));
    }

    @Override
    public void useWith(Repairable target) {
        if (target == null) return;

        target.repair();          // ✔️ ремонт
        super.useWith(target);    // ✔️ зменшуємо durability інструмента
    }

    @Override
    public Class<Repairable> getUsingActorClass() {
        return Repairable.class;
    }
}
