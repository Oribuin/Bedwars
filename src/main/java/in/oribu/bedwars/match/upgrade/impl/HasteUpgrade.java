package in.oribu.bedwars.match.upgrade.impl;

import in.oribu.bedwars.match.Match;
import in.oribu.bedwars.match.MatchPlayer;
import in.oribu.bedwars.match.Team;
import in.oribu.bedwars.match.upgrade.Upgrade;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Gives haste to all players on the team
 */
public class HasteUpgrade extends Upgrade {

    @Override
    public void equip(Match match, Team team, int level) {
        team.getPlayers().values()
                .stream()
                .map(MatchPlayer::getPlayer)
                .forEach(player -> player.addPotionEffect(
                        new PotionEffect(
                                PotionEffectType.FAST_DIGGING,
                                Integer.MAX_VALUE,
                                level - 1,
                                false,
                                false,
                                true
                        )
                ));
    }

}
