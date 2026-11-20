package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class ChainBomb extends TimeBomb {
    private static final float RADIUS = 50f;

    public ChainBomb(float delaySeconds) {
        super(delaySeconds);
    }

    @Override
    protected void onDetonate() {
        Scene scene = getScene();
        if (scene == null) return;

        float centerX = getPosX() + (getWidth() / 2f);
        float centerY = getPosY() + (getHeight() / 2f);
        Ellipse2D.Float blast = new Ellipse2D.Float(centerX - RADIUS, centerY - RADIUS, RADIUS * 2, RADIUS * 2);

        for (Actor actor : scene.getActors()) {
            if (actor == this) continue;
            if (!(actor instanceof ChainBomb)) continue;
            ChainBomb other = (ChainBomb) actor;
            if (other.isActivated()) continue; // already ticking
            Rectangle2D.Float otherBounds = new Rectangle2D.Float(other.getPosX(), other.getPosY(), other.getWidth(), other.getHeight());
            if (blast.intersects(otherBounds)) {
                other.activate();
            }
        }
    }
}
