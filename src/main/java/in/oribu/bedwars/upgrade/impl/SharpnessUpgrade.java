package in.oribu.bedwars.upgrade.impl;

import in.oribu.bedwars.match.Match;
import in.oribu.bedwars.match.MatchPlayer;
import in.oribu.bedwars.match.Team;
import in.oribu.bedwars.upgrade.Upgrade;
import org.bukkit.Tag;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;

/**
 * Gives sharpness to all players on the team
 */
public class SharpnessUpgrade extends Upgrade {

    @Override
    public void equip(Match match, Team team, int level) {
        team.getPlayers().values()
                .stream()
                .map(MatchPlayer::getPlayer)
                .forEach(player -> Arrays.stream(player.getInventory().getContents())
                        .filter(itemStack -> itemStack != null && !itemStack.getType().isAir())
                        .filter(itemStack -> Tag.ITEMS_SWORDS.isTagged(itemStack.getType()) || Tag.ITEMS_AXES.isTagged(itemStack.getType()))
                        .forEach(itemStack -> itemStack.addEnchantment(Enchantment.DAMAGE_ALL, level)));
    }

}
