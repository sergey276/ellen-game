package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.When;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.oop.game.characters.Alien;
import sk.tuke.kpi.oop.game.characters.Ripley;

public class SpawnPoint extends AbstractActor {

    private int remainingSpawns;

    public SpawnPoint(int spawnCount) {
        this.remainingSpawns = spawnCount;
        setAnimation(new Animation("sprites/spawn.png", 32, 32));
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);

        
        new When<>(
            this::isPlayerNearby,
            new Invoke<>(() -> {
                if (remainingSpawns > 0) {
                    spawnAlien();
                    scheduleNextSpawn();
                }
            })
        ).scheduleFor(this);
    }

    private boolean isPlayerNearby() {
        Scene scene = getScene();
        if (scene == null) return false;

        Ripley ripley = scene.getFirstActorByType(Ripley.class);
        if (ripley == null) return false;

        float dx = ripley.getPosX() - getPosX();
        float dy = ripley.getPosY() - getPosY();
        return Math.sqrt(dx * dx + dy * dy) <= 50;
    }

    private void spawnAlien() {
        Scene scene = getScene();
        if (scene == null) return;
        if (remainingSpawns <= 0) return;

        Alien alien = new Alien();
        scene.addActor(alien, getPosX(), getPosY());
        remainingSpawns--;
    }

    private void scheduleNextSpawn() {
        
        if (remainingSpawns <= 0) return;

        new ActionSequence<>(
            new Wait<>(3),                 
            new Invoke<>(() -> {           
                if (isPlayerNearby() && remainingSpawns > 0) {
                    spawnAlien();
                    
                    scheduleNextSpawn();
                }
            })
        ).scheduleFor(this);
    }
}
