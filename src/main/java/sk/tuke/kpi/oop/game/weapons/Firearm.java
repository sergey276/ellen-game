package sk.tuke.kpi.oop.game.weapons;

public abstract class Firearm {
    private int ammo;
    private int maxAmmo;

    public Firearm(int initialAmmo, int maxAmmo) {
        this.ammo = initialAmmo;
        this.maxAmmo = maxAmmo;
    }

    public Firearm(int ammo) {
        this(ammo, ammo);
    }

    public int getAmmo() {
        return ammo;
    }

    public void reload(int newAmmo) {
        ammo = Math.min(maxAmmo, ammo + newAmmo);
    }

    public Fireable fire() {
        if (ammo > 0) {
            ammo--;
            return createBullet();
        }
        return null;
    }

    protected abstract Fireable createBullet();
}
