package prj.item;

public class Bazooka extends Item{

    private int damageRadius;
    private int damageDropOff;

    public Bazooka() {
        super("Bazooka", "BazookaIcon", 0);
    }

    public int getDamageRadius() {
        return damageRadius;
    }

    public void setDamageRadius(int damageRadius) {
        this.damageRadius = damageRadius;
    }

    public int getDamageDropOff() {
        return damageDropOff;
    }

    public void setDamageDropOff(int damageDropOff) {
        this.damageDropOff = damageDropOff;
    }
}
