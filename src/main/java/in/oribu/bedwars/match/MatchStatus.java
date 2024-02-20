package in.oribu.bedwars.match;

public enum MatchStatus {
    /**
     * The match is waiting for players to join
     */
    WAITING,

    /**
     * The match is starting
     */
    RUNNING,

    /**
     * The match has ended, we are cleaning up and preparing for the next match
     */
    ENDING

}
