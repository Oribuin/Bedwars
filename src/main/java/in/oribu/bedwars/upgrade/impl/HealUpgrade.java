package in.oribu.bedwars.upgrade.impl;

import in.oribu.bedwars.match.Match;
import in.oribu.bedwars.match.Team;
import in.oribu.bedwars.upgrade.Upgrade;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.TimeUnit;

/**
 * Heals all players on the team while on island
 */
public class HealUpgrade extends Upgrade {

    private final long cooldown = TimeUnit.SECONDS.toMillis(5);
    private long lastUse = System.currentTimeMillis();

    @Override
    public void tick(Match match, Team team, int level) {
        if (System.currentTimeMillis() - this.lastUse < this.cooldown)
            return;

        this.lastUse = System.currentTimeMillis();

        int radius = match.getMap().getIslandRadius();

        Location center = team.getSpawn();
        if (center.getWorld() != null) {
            center.getWorld().spawnParticle(
                    Particle.VILLAGER_HAPPY,
                    center.clone(),
                    100,
                    radius,
                    radius,
                    radius,
                    0
            );
        }

        center.getWorld().getNearbyEntities(center.clone(), radius, radius, radius,
                entity ->
                        team.getPlayers().containsKey(entity.getUniqueId())
                        && entity instanceof LivingEntity
        ).forEach(entity -> ((LivingEntity) entity).addPotionEffect(new PotionEffect(
                PotionEffectType.REGENERATION,
                20 * 10,
                0,
                false,
                false,
                true
        )));
    }

}
