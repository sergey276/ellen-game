package sk.tuke.kpi.oop.game.scenarios;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.ActorFactory;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.SceneListener;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.oop.game.Locker;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.items.Energy;
import sk.tuke.kpi.oop.game.items.AccessCard;
import sk.tuke.kpi.oop.game.openables.LockedDoor;
import sk.tuke.kpi.oop.game.Ventilator;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.controllers.KeeperController;


public class MissionImpossible implements SceneListener {

    private Ripley ripley;

    private Disposable energyDrain;
    private Disposable moveListener;
    private Disposable keeperListener;

    public static class Factory implements ActorFactory {

        @Override
        public Actor create(String type, String name) {

            if ("ellen".equalsIgnoreCase(name)) return new Ripley();
            if ("energy".equalsIgnoreCase(name)) return new Energy();
            if ("ventilator".equalsIgnoreCase(name)) return new Ventilator();
            if ("door".equalsIgnoreCase(name)) return new LockedDoor();
            if ("access card".equalsIgnoreCase(name)) return new AccessCard();
            if ("locker".equalsIgnoreCase(name)) return new Locker();

            return null;
        }
    }

    @Override
    public void sceneInitialized(Scene scene) {

        ripley = scene.getFirstActorByType(Ripley.class);

        scene.follow(ripley);

        moveListener = scene.getInput().registerListener(new MovableController(ripley));
        keeperListener = scene.getInput().registerListener(new KeeperController(ripley));

        scene.getGame().pushActorContainer(ripley.getBackpack());



        energyDrain = ripley.new EnergyDrainAction().scheduleFor(ripley);

        scene.getMessageBus().subscribe(Ripley.RIPLEY_DIED, r -> {
            if (moveListener != null) moveListener.dispose();
            if (keeperListener != null) keeperListener.dispose();
            if (energyDrain != null) energyDrain.dispose();
        });

        scene.getMessageBus().subscribe(Ventilator.VENTILATOR_REPAIRED, v -> {
            if (energyDrain != null) energyDrain.dispose();
        });
    }

    @Override
    public void sceneUpdating(Scene scene) {
        if (ripley != null) {
            ripley.showRipleyState();
        }
    }
}
