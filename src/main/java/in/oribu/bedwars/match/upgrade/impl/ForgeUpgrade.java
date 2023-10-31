package in.oribu.bedwars.match.upgrade.impl;

import in.oribu.bedwars.match.Match;
import in.oribu.bedwars.match.Team;
import in.oribu.bedwars.match.upgrade.Upgrade;

public class ForgeUpgrade implements Upgrade {

    @Override
    public void tick(Match match, Team team, int level) {
        // TODO: Get team island generator.
    }

    @Override
    public void equip(Match match, Team team, int level) {
        // Unused
    }

    @Override
    public void remove(Match match, Team team) {
        // Unused
    }

}
