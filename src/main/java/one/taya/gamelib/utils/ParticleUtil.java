package one.taya.gamelib.utils;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ParticleUtil {
   
    public static void drawLine(Location point1, Location point2, double space, Player player) {
        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        
        for(double covered = 0; covered < distance; p1.add(vector)) {
            player.spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ(), 0, 0, 0, 0, new Particle.DustOptions(Color.AQUA, 1));
            covered += space;
        }
    }
   
    public static void drawBox(Location point1, Location point2, double space, Player player) {
        ParticleUtil.drawLine(new Location(point1.getWorld(), point1.getX(), point1.getY(), point1.getZ()), new Location(point1.getWorld(), point2.getX(), point1.getY(), point1.getZ()), space, player);
        ParticleUtil.drawLine(new Location(point1.getWorld(), point2.getX(), point1.getY(), point1.getZ()), new Location(point1.getWorld(), point2.getX(), point2.getY(), point1.getZ()), space, player);
        ParticleUtil.drawLine(new Location(point1.getWorld(), point2.getX(), point2.getY(), point1.getZ()), new Location(point1.getWorld(), point1.getX(), point2.getY(), point1.getZ()), space, player);
        ParticleUtil.drawLine(new Location(point1.getWorld(), point1.getX(), point2.getY(), point1.getZ()), new Location(point1.getWorld(), point1.getX(), point1.getY(), point1.getZ()), space, player);
        ParticleUtil.drawLine(new Location(point1.getWorld(), point1.getX(), point1.getY(), point1.getZ()), new Location(point1.getWorld(), point1.getX(), point1.getY(), point2.getZ()), space, player);
        ParticleUtil.drawLine(new Location(point1.getWorld(), point2.getX(), point1.getY(), point1.getZ()), new Location(point1.getWorld(), point2.getX(), point1.getY(), point2.getZ()), space, player);
        ParticleUtil.drawLine(new Location(point1.getWorld(), point1.getX(), point2.getY(), point1.getZ()), new Location(point1.getWorld(), point1.getX(), point2.getY(), point2.getZ()), space, player);
        ParticleUtil.drawLine(new Location(point1.getWorld(), point2.getX(), point2.getY(), point1.getZ()), new Location(point1.getWorld(), point2.getX(), point2.getY(), point2.getZ()), space, player);
        ParticleUtil.drawLine(new Location(point1.getWorld(), point1.getX(), point1.getY(), point2.getZ()), new Location(point1.getWorld(), point2.getX(), point1.getY(), point2.getZ()), space, player);
        ParticleUtil.drawLine(new Location(point1.getWorld(), point2.getX(), point1.getY(), point2.getZ()), new Location(point1.getWorld(), point2.getX(), point2.getY(), point2.getZ()), space, player);
        ParticleUtil.drawLine(new Location(point1.getWorld(), point2.getX(), point2.getY(), point2.getZ()), new Location(point1.getWorld(), point1.getX(), point2.getY(), point2.getZ()), space, player);
        ParticleUtil.drawLine(new Location(point1.getWorld(), point1.getX(), point2.getY(), point2.getZ()), new Location(point1.getWorld(), point1.getX(), point1.getY(), point2.getZ()), space, player);
    }
}
