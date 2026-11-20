package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.Reactor;

public class FireExtinguisher extends BreakableTool<Reactor>
    implements Collectible, Usable<Reactor> {

    public FireExtinguisher() {
        super(1);
        setAnimation(new Animation("sprites/extinguisher.png", 16, 16));
    }

    @Override
    public void useWith(Reactor reactor) {
        if (reactor == null) return;

        reactor.extinguish();   // гасимо
        super.useWith(reactor); // зменшуємо міцність інструменту
    }

    @Override
    public Class<Reactor> getUsingActorClass() {
        return Reactor.class;
    }
}
