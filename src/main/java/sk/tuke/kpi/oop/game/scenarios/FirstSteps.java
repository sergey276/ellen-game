package sk.tuke.kpi.oop.game.scenarios;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.SceneListener;
import sk.tuke.kpi.gamelib.graphics.Overlay;
import sk.tuke.kpi.gamelib.GameApplication;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.items.*;

public class FirstSteps implements SceneListener {
    private Ripley ripley;

    @Override
    public void sceneInitialized(Scene scene) {
        ripley = new Ripley();
        scene.addActor(ripley, 100, 100);

        var moveCtrl = new MovableController(ripley);
        scene.getInput().registerListener(moveCtrl);

        var keeperCtrl = new KeeperController(ripley);
        scene.getInput().registerListener(keeperCtrl);

        scene.getGame().pushActorContainer(ripley.getBackpack());

        Energy energy = new Energy();
        scene.addActor(energy, 200, 100);

        Ammo ammo = new Ammo();
        scene.addActor(ammo, 240, 100);

        Hammer hammer = new Hammer();
        scene.addActor(hammer, 280, 100);

        Wrench wrench = new Wrench();
        scene.addActor(wrench, 320, 100);

        FireExtinguisher extinguisher = new FireExtinguisher();
        scene.addActor(extinguisher, 360, 100);

        Mjolnir mjolnir = new Mjolnir();
        scene.addActor(mjolnir, 400, 100);
    }

    @Override
    public void sceneUpdating(Scene scene) {
        Overlay overlay = scene.getGame().getOverlay();
        int windowHeight = scene.getGame().getWindowSetup().getHeight();
        int yTextPos = windowHeight - GameApplication.STATUS_LINE_OFFSET;
        overlay.drawText("  | Health: " + ripley.getHealth().getValue() + " | Ammo: " + ripley.getFirearm().getAmmo(), 100, yTextPos);
    }
}
