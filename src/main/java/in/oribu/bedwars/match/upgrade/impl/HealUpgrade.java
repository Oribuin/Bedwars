package in.oribu.bedwars.match.upgrade.impl;

import in.oribu.bedwars.match.Match;
import in.oribu.bedwars.match.MatchPlayer;
import in.oribu.bedwars.match.Team;
import in.oribu.bedwars.match.upgrade.Upgrade;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.TimeUnit;

public class HealUpgrade implements Upgrade {

    private final long cooldown = TimeUnit.SECONDS.toMillis(5);
    private long lastUse = System.currentTimeMillis();

    @Override
    public void equip(Match match, Team team, int level) {

    }

    @Override
    public void tick(Match match, Team team, int level) {
        if (System.currentTimeMillis() - this.lastUse < this.cooldown)
            return;

        this.lastUse = System.currentTimeMillis();

        // TODO: get map radius;
        int radius = match.

        final Location center = team.getSpawn();
        if (center.getWorld() != null) {
            center.getWorld().spawnParticle(
                    Particle.VILLAGER_HAPPY,
                    center.clone(),
                    100,


            );
        }

        team.getPlayers().values()
                .stream()
                .map(MatchPlayer::getPlayer)
                .forEach(player -> player.addPotionEffect(new PotionEffect(
                        PotionEffectType.REGENERATION,
                        20 * 10,
                        0,
                        false,
                        false,
                        true
                )));

    }

    @Override
    public void remove(Match match, Team team) {

    }

}
