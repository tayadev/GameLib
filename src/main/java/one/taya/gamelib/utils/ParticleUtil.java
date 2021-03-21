package one.taya.gamelib.utils;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ParticleUtil {
   
    public static void drawLine(Location point1, Location point2, double space, Player player) {
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        double distance = p1.distance(p2);
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        
        int particleCounter = 0;

        for(double covered = 0; covered < distance; p1.add(vector)) {
            // GameLib.logDebug(String.valueOf(p1.clone().subtract(player.getLocation().toVector()).dot(player.getLocation().getDirection())), GameLib.getPlugin());
            if(particleCounter < 1000 && (p1.clone().subtract(player.getLocation().toVector())).dot(player.getLocation().getDirection()) > 0) { // Limit to 1000 particles and only display the ones in front of the player 
                player.spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ(), 0, 0, 0, 0, new Particle.DustOptions(Color.AQUA, 1));
                particleCounter++;
            }
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
