package in.oribu.bedwars.match.upgrade.impl;

import in.oribu.bedwars.match.Match;
import in.oribu.bedwars.match.MatchPlayer;
import in.oribu.bedwars.match.Team;
import in.oribu.bedwars.match.upgrade.Upgrade;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;

public class ProtectionUpgrade implements Upgrade {

    @Override
    public void equip(Match match, Team team, int level) {
        team.getPlayers().values()
                .stream()
                .map(MatchPlayer::getPlayer)
                .forEach(player -> Arrays.stream(player.getInventory().getArmorContents())
                        .forEach(itemStack -> itemStack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, level)));
    }

    @Override
    public void tick(Match match, Team team, int level) {
        // Unused

    }

    @Override
    public void remove(Match match,  Team team) {
        // Unused
    }

}
