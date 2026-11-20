package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.Player;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Helicopter extends AbstractActor {
    private Disposable chaseLoop;

    public Helicopter() {
        setAnimation(new Animation("sprites/Helicopter.png", 64, 64, 0.2f, Animation.PlayMode.LOOP_PINGPONG));
    }

    public void searchAndDestroy() {
        if (chaseLoop != null) return;
        chaseLoop = new Loop<>(new Invoke<>(this::tickChase)).scheduleFor(this);
    }

    private void tickChase() {
        Scene scene = getScene();
        if (scene == null) return;

        Player target = findFrameworkPlayer(scene);
        if (target == null) return;

        float hx = getPosX();
        float hy = getPosY();
        float tx = target.getPosX();
        float ty = target.getPosY();

        float dx = tx - hx;
        float dy = ty - hy;
        float dist = (float) Math.hypot(dx, dy);
        float step = 2.5f;

        if (dist > 0.0001f) {
            setPosition((int)(hx + step * dx / dist), (int)(hy + step * dy / dist));
        }

        // зменшуємо енергію при зіткненні
        if (intersects(target)) {
            int currentEnergy = target.getEnergy();
            if (currentEnergy > 0) {
                target.setEnergy(Math.max(0, currentEnergy - 1));
            }
        }
    }

    @Override
    public boolean intersects(Actor other) {
        return getPosX() < other.getPosX() + other.getWidth()
            && getPosX() + getWidth() > other.getPosX()
            && getPosY() < other.getPosY() + other.getHeight()
            && getPosY() + getHeight() > other.getPosY();
    }

    private Player findFrameworkPlayer(Scene scene) {
        for (Actor actor : scene.getActors()) {
            if (actor instanceof Player) {
                return (Player) actor;
            }
        }
        return null;
    }
}
