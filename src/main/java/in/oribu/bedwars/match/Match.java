package in.oribu.bedwars.match;

import in.oribu.bedwars.match.generator.Generator;
import in.oribu.bedwars.match.map.Map;

import java.util.ArrayList;
import java.util.List;

public class Match {

    private final Map map; // The map of the match
    private final List<Team> teams; // The teams in the match
    private long startTime; // The time the match started

    public Match(Map map) {
        this.map = map;
        this.teams = new ArrayList<>();
        this.startTime = 0;
    }

    public void load() {
        this.map.load();
    }

    /**
     * Start the match timer (called when the match starts)
     */
    public void start() {
        // Set the start time
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Tick the match (called every tick)
     */
    public void tick() {
        // Tick the generators
        this.map.getGenerators().forEach(Generator::tick);
        this.teams.forEach(team -> team.tick(this));
    }

    public Map getMap() {
        return map;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

}


