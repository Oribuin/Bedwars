package in.oribu.bedwars.match.upgrade;

import in.oribu.bedwars.match.Match;
import in.oribu.bedwars.match.Team;

/**
 * Represents an upgrade
 */
public interface Upgrade {

    /**
     * The method for each upgrade to be called when it is applied
     *
     * @param level The level of the upgrade
     */
    void equip(Match match, Team team, int level);

    /**
     * The method for each upgrade to be called when it is removed
     */
    void tick(Match match, Team team, int level);

    /**
     * The method for each upgrade to be called when it is removed
     */
    void remove(Match match, Team team);

}
