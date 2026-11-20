package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class TimeBomb extends AbstractActor {
    private final float delaySeconds;
    private boolean activated;
    private boolean detonated;

    private final Animation activatedAnimation;
    private final Animation explosionAnimation;

    public TimeBomb(float delaySeconds) {
        this.delaySeconds = Math.max(0f, delaySeconds);

        this.activated = false;
        this.detonated = false;

        setAnimation(new Animation("sprites/bomb.png"));
        this.activatedAnimation = new Animation("sprites/bomb_activated.png", 16, 16, 0.05f, Animation.PlayMode.LOOP_PINGPONG);
        // Always use an existing explosion resource to avoid missing-asset crashes in headless tests
        this.explosionAnimation = new Animation("sprites/small_explosion.png", 16, 16, 0.05f, Animation.PlayMode.ONCE);
    }

    public void activate() {
        if (activated || detonated) return;
        activated = true;
        setAnimation(activatedAnimation);
        new ActionSequence<>(
            new Wait<>(delaySeconds),
            new Invoke<>(this::detonate)
        ).scheduleFor(this);
    }

    public boolean isActivated() {
        return activated && !detonated;
    }

    protected void detonate() {
        if (detonated) return;
        detonated = true;
        activated = false;
        setAnimation(explosionAnimation);
        float explosionDuration = explosionAnimation.getFrameCount() * explosionAnimation.getFrameDuration();
        new ActionSequence<>(
            new Wait<>(explosionDuration),
            new Invoke<>(this::removeFromScene)
        ).scheduleFor(this);
        onDetonate();
    }

    private void removeFromScene() {
        Scene scene = getScene();
        if (scene != null) scene.removeActor(this);
    }

    protected void onDetonate() {
    }
}
