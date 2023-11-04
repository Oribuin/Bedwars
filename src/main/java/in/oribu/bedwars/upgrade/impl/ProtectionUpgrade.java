package in.oribu.bedwars.upgrade.impl;

import in.oribu.bedwars.match.Match;
import in.oribu.bedwars.match.MatchPlayer;
import in.oribu.bedwars.match.Team;
import in.oribu.bedwars.upgrade.Upgrade;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;

/**
 * Gives protection to all players on the team
 */
public class ProtectionUpgrade extends Upgrade {

    @Override
    public void equip(Match match, Team team, int level) {
        team.getPlayers().values()
                .stream()
                .map(MatchPlayer::getPlayer)
                .forEach(player -> Arrays.stream(player.getInventory().getArmorContents())
                        .forEach(itemStack -> {
                            if (itemStack == null) return;

                            itemStack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, level);
                        }));
    }

}
