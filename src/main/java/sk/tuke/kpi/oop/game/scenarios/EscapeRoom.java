package sk.tuke.kpi.oop.game.scenarios;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.SceneListener;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.controllers.MovableController;
import sk.tuke.kpi.oop.game.controllers.KeeperController;
import sk.tuke.kpi.oop.game.controllers.ShooterController;
import sk.tuke.kpi.oop.game.openables.Door;

public class EscapeRoom implements SceneListener {

    private Ripley ripley;
    private Disposable moveListener;
    private Disposable keeperListener;
    private Disposable shooterListener;
    private boolean showWinMessage = false;

    @Override
    public void sceneInitialized(Scene scene) {
        ripley = scene.getFirstActorByType(Ripley.class);

        if (ripley != null) {
            scene.follow(ripley);

            moveListener = scene.getInput().registerListener(new MovableController(ripley));
            keeperListener = scene.getInput().registerListener(new KeeperController(ripley));
            shooterListener = scene.getInput().registerListener(new ShooterController(ripley));

            scene.getGame().pushActorContainer(ripley.getBackpack());

            scene.getMessageBus().subscribe(Ripley.RIPLEY_DIED, r -> {
                if (moveListener != null) moveListener.dispose();
                if (keeperListener != null) keeperListener.dispose();
                if (shooterListener != null) shooterListener.dispose();
            });
        }

        scene.getMessageBus().subscribe(Door.DOOR_OPENED, door -> {
            if (door.getName().equals("exit door")) {
                showWinMessage = true;
                if (moveListener != null) moveListener.dispose();
                if (keeperListener != null) keeperListener.dispose();
                if (shooterListener != null) shooterListener.dispose();
            }
        });
    }

    @Override
    public void sceneUpdating(Scene scene) {
        if (ripley != null) {
            ripley.showRipleyState();
        }

        if (showWinMessage) {
            scene.getGame().getOverlay().drawText("You won! Good job!", 200, 300);
        }
    }
}
