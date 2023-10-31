package in.oribu.bedwars.upgrade;

import in.oribu.bedwars.match.Match;
import in.oribu.bedwars.match.Team;

/**
 * Represents an upgrade
 */
public abstract class Upgrade {

    /**
     * The method for each upgrade to be called when it is applied
     *
     * @param level The level of the upgrade
     */
    public void equip(Match match, Team team, int level) {
        // Unused
    }

    /**
     * The method for each upgrade to be called when it is removed
     */
    public void tick(Match match, Team team, int level) {
        // Unused
    }

    /**
     * The method for each upgrade to be called when it is removed
     */
    public void remove(Match match, Team team) {
        // Unused
    }

    /**
     * The method that is called when an event is fired
     *
     * @param handler The context handler
     */
    public void event(ContextHandler handler, Team team, int level) {
        // Unused
    }

}
