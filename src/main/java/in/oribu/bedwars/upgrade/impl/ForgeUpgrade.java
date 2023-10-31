package in.oribu.bedwars.upgrade.impl;

import in.oribu.bedwars.match.Match;
import in.oribu.bedwars.match.Team;
import in.oribu.bedwars.upgrade.Upgrade;
import org.bukkit.Material;

/**
 * Upgrades the resource count for the team's generator
 */
public class ForgeUpgrade extends Upgrade {

    @Override
    public void equip(Match match, Team team, int level) {
        switch (level) {
            case 1 -> team.getGenerator().upgrade(Material.IRON_INGOT);
            case 2 -> team.getGenerator().upgrade(Material.GOLD_INGOT);
            case 3 -> team.getGenerator().upgrade(Material.EMERALD);
        }
    }

}
