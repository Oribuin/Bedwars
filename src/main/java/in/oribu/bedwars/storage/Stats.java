package in.oribu.bedwars.storage;

import in.oribu.bedwars.match.MatchPlayer;

/**
 * Storage class for all the global stats for each player
 */
public class Stats {

    private int kills = 0;
    private int deaths = 0;
    private int wins = 0;
    private int losses = 0;
    private int bedsBroken = 0;
    private int bedsLost = 0;

    /**
     * Increment these stats by the stats of the player
     *
     * @param player The player
     */
    public void add(MatchPlayer player) {
        Stats stats = player.getStats();

        this.kills += stats.getKills();
        this.deaths += stats.getDeaths();
        this.wins += stats.getWins();
        this.losses += stats.getLosses();
        this.bedsBroken += stats.getBedsBroken();
        this.bedsLost += stats.getBedsLost();
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getBedsBroken() {
        return bedsBroken;
    }

    public void setBedsBroken(int bedsBroken) {
        this.bedsBroken = bedsBroken;
    }

    public int getBedsLost() {
        return bedsLost;
    }

    public void setBedsLost(int bedsLost) {
        this.bedsLost = bedsLost;
    }

}