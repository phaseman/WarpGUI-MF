package ac.minef.warpgui.particles;

import ac.minef.warpgui.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Particle {
    private ParticleEffect particleName;

    private Location loc;

    private float[] data;

    private int amount;

    public Particle(Location loc, ParticleEffect particleName, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        this.loc = loc.add(new Vector(0.5D, 0.5D, 0.5D));
        this.particleName = particleName;
        (this.data = new float[4])[0] = offsetX;
        this.data[1] = offsetY;
        this.data[2] = offsetZ;
        this.data[3] = speed;
        this.amount = amount;
    }

    public static void create(Location loc, ParticleEffect particleName, float offsetX, float offsetY, float offsetZ, float speed, int amount, boolean repeating) {
        Particle particle = new Particle(loc, particleName, offsetX, offsetY, offsetZ, speed, amount);
        if (repeating) {
            particle.start();
        } else {
            particle.display();
        }
    }

    private void start() {
        (new BukkitRunnable() {
            public void run() {
                try {
                    if (Particle.this.loc.getWorld().getPlayers().isEmpty())
                        return;
                    for (Player p : Particle.this.loc.getWorld().getPlayers()) {
                        if (p.getLocation().distance(Particle.this.loc) <= 16.0D)
                            Particle.this.particleName.display(Particle.this.data[0], Particle.this.data[1], Particle.this.data[2], Particle.this.data[3], Particle.this.amount, Particle.this.loc, new Player[] { p });
                    }
                } catch (Exception exception) {}
            }
        }).runTaskTimerAsynchronously((Plugin) Main.getInstance(), 5L, 5L);
    }

    private void display() {
        try {
            if (this.loc.getWorld().getPlayers().isEmpty())
                return;
            for (Player p : this.loc.getWorld().getPlayers()) {
                if (p.getLocation().distance(this.loc) <= 16.0D)
                    this.particleName.display(this.data[0], this.data[1], this.data[2], this.data[3], this.amount, this.loc, new Player[] { p });
            }
        } catch (Exception exception) {}
    }
}