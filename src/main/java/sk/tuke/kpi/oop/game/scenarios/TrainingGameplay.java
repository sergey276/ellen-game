package sk.tuke.kpi.oop.game.scenarios;

import sk.tuke.kpi.oop.game.Cooler;
import sk.tuke.kpi.oop.game.Reactor;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.GameApplication;
import sk.tuke.kpi.gamelib.framework.Scenario;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.oop.game.items.FireExtinguisher;
import sk.tuke.kpi.oop.game.items.Hammer;
import sk.tuke.kpi.oop.game.actions.Use;
import sk.tuke.kpi.oop.game.items.Energy;
import sk.tuke.kpi.oop.game.items.Ammo;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.gamelib.Actor;
public class TrainingGameplay extends Scenario {

    @Override
    public void setupPlay(Scene scene) {
        Reactor reactor = new Reactor();
        scene.addActor(reactor, 146, 96);
        reactor.turnOn();

        Cooler cooler = new Cooler();
        scene.addActor(cooler, 40, 40);

        new ActionSequence<>(
            new Wait<>(5),
            new Invoke<>(cooler::turnOn)
        ).scheduleFor(cooler);

        Hammer hammer = new Hammer();
        scene.addActor(hammer, 100, 96);

        new When<>(
            () -> reactor.getTemperature() >= 3000,
            new Invoke<>(() -> hammer.useWith(reactor))
        ).scheduleFor(reactor);

        FireExtinguisher fireExtinguisher = new FireExtinguisher();
        scene.addActor(fireExtinguisher, 100, 140);

        new When<>(
            () -> reactor.getDamage() >= 100,
            new Invoke<>(() -> fireExtinguisher.useWith(reactor))
        ).scheduleFor(reactor);

        Ripley ripley = new Ripley();
        scene.addActor(ripley, 50, 50);
        scene.getInput().registerListener(new MovableController(ripley));
        scene.getInput().registerListener(new KeeperController(ripley));

        Energy energy = new Energy();
        scene.addActor(energy, 60, 60);

        Ammo ammo = new Ammo();
        scene.addActor(ammo, 80, 60);
    }

    @Override
    public void sceneUpdating(Scene scene) {
        super.sceneUpdating(scene);

        Ripley ripley = scene.getFirstActorByType(Ripley.class);
        if (ripley == null) return;

        int windowHeight = scene.getGame().getWindowSetup().getHeight();
        int yTextPos = windowHeight - GameApplication.STATUS_LINE_OFFSET;
        scene.getGame().getOverlay().drawText(
            "Health: " + ripley.getHealth().getValue() + "   Ammo: " + ripley.getFirearm().getAmmo(),
            10, yTextPos
        );
    }
}
