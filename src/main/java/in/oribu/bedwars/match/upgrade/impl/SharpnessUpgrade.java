package in.oribu.bedwars.match.upgrade.impl;

import in.oribu.bedwars.match.Match;
import in.oribu.bedwars.match.MatchPlayer;
import in.oribu.bedwars.match.Team;
import in.oribu.bedwars.match.upgrade.Upgrade;
import org.bukkit.Tag;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;

public class SharpnessUpgrade implements Upgrade {

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

    @Override
    public void tick(Match match, Team team, int level) {
        // Unused
    }

    @Override
    public void remove(Match match, Team team) {
        // Unused
    }

}
