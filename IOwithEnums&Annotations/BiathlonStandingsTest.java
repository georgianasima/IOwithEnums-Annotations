package org.example.IOwithAnnotationsAndEnums;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class BiathlonStandingsTest {
    private static final double DELTA = 1e-15;
    @Test
    public void testCalculatePenalty() {
        int penalty1 = BiathlonStandings.calculatePenalty("xxxxx");
        assertEquals(0, penalty1);
        int penalty2 = BiathlonStandings.calculatePenalty("oooxx");
        assertEquals(30, penalty2);
    }
    @Test
    public void testConvertTimeToSeconds() {
        int time1 = BiathlonStandings.convertTimeToSeconds("00:00");
        assertEquals(0, time1);

        int time2 = BiathlonStandings.convertTimeToSeconds("01:30");
        assertEquals(90, time2);

        int time3 = BiathlonStandings.convertTimeToSeconds("05:45");
        assertEquals(345, time3);
    }
    @Test
    public void testCalculateStandings() {
        BiathlonStandings.BiathlonAthlete athlete1 = new BiathlonStandings.BiathlonAthlete(11, "Umar Jorgson", "SK",
                "30:27", "xxxox", "xxxxx", "xxoxo");
        BiathlonStandings.BiathlonAthlete athlete2 = new BiathlonStandings.BiathlonAthlete(1, "Jimmy Smiles", "UK",
                "29:15", "xxoox", "xooxo", "xxxxo");
        BiathlonStandings.BiathlonAthlete athlete3 = new BiathlonStandings.BiathlonAthlete(27, "Piotr Smitzer", "CZ",
                "30:10", "xxxxx", "xxxxx", "xxxxx");

        List<BiathlonStandings.BiathlonAthlete> athletes = new ArrayList<>();
        athletes.add(athlete1);
        athletes.add(athlete2);
        athletes.add(athlete3);

        List<BiathlonStandings.BiathlonAthlete> standings = BiathlonStandings.calculateStandings(athletes);
        assertEquals(athlete2, standings.get(0)); // Winner
        assertEquals(athlete3, standings.get(1)); // Runner-up
        assertEquals(athlete1, standings.get(2)); // Third place
    }
    @Test
    public void testGetPodium() {
        BiathlonStandings.BiathlonAthlete athlete1 = new BiathlonStandings.BiathlonAthlete(11, "Umar Jorgson", "SK",
                "30:27", "xxxox", "xxxxx", "xxoxo");
        BiathlonStandings.BiathlonAthlete athlete2 = new BiathlonStandings.BiathlonAthlete(1, "Jimmy Smiles", "UK",
                "29:15", "xxoox", "xooxo", "xxxxo");
        BiathlonStandings.BiathlonAthlete athlete3 = new BiathlonStandings.BiathlonAthlete(27, "Piotr Smitzer", "CZ",
                "30:10", "xxxxx", "xxxxx", "xxxxx");
        List<BiathlonStandings.BiathlonAthlete> standings = new ArrayList<>();
        standings.add(athlete2);
        standings.add(athlete3);
        standings.add(athlete1);

        Map<BiathlonStandings.Ranking, BiathlonStandings.BiathlonAthlete> podium = BiathlonStandings.getPodium(standings);

        assertEquals(athlete2, podium.get(BiathlonStandings.Ranking.WINNER));
        assertEquals(athlete3, podium.get(BiathlonStandings.Ranking.RUNNER_UP));
        assertEquals(athlete1, podium.get(BiathlonStandings.Ranking.THIRD_PLACE));
    }

}