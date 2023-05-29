package org.example.IOwithAnnotationsAndEnums;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
public class BiathlonStandings {
    public static void main(String[] args) {
        String csvData = "11,Umar Jorgson,SK,30:27,xxxox,xxxxx,xxoxo\n" +
                "1,Jimmy Smiles,UK,29:15,xxoox,xooxo,xxxxo\n" +
                "27,Piotr Smitzer,CZ,30:10,xxxxx,xxxxx,xxxxx";
        try {
            List<BiathlonAthlete> athletes = parseCSV(csvData);
            List<BiathlonAthlete> standings = calculateStandings(athletes);
            Map<Ranking, BiathlonAthlete> podium = getPodium(standings);

            System.out.println("Winner - " + podium.get(Ranking.WINNER));
            System.out.println("Runner-up - " + podium.get(Ranking.RUNNER_UP));
            System.out.println("Third Place - " + podium.get(Ranking.THIRD_PLACE));
        } catch (IOException e) {
            System.out.println("Failed to parse CSV data: " + e.getMessage());
        }
    }
    public static List<BiathlonAthlete> parseCSV(String csv) throws IOException {
        List<BiathlonAthlete> athletes = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new StringReader(csv));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            int athleteNumber = Integer.parseInt(parts[0]);
            String athleteName = parts[1];
            String countryCode = parts[2];
            String skiTimeResult = parts[3];
            String firstShootingRange = parts[4];
            String secondShootingRange = parts[5];
            String thirdShootingRange = parts[6];

            int totalTime = convertTimeToSeconds(skiTimeResult) + calculatePenalty(firstShootingRange)
                    + calculatePenalty(secondShootingRange) + calculatePenalty(thirdShootingRange);

            BiathlonAthlete athlete = new BiathlonAthlete(athleteNumber, athleteName, countryCode, skiTimeResult,
                    firstShootingRange, secondShootingRange, thirdShootingRange);
            athlete.setTotalTime(totalTime);
            athletes.add(athlete);
        }
        return athletes;
    }
    public static List<BiathlonAthlete> calculateStandings(List<BiathlonAthlete> athletes) {
        List<BiathlonAthlete> standings = new ArrayList<>(athletes);
        for (BiathlonAthlete athlete : standings) {
            int skiTimeSeconds = convertTimeToSeconds(athlete.getSkiTimeResult());
            int penaltySeconds = calculatePenalty(athlete.getFirstShootingRange())
                    + calculatePenalty(athlete.getSecondShootingRange())
                    + calculatePenalty(athlete.getThirdShootingRange());
            athlete.setTotalTime(skiTimeSeconds + penaltySeconds);
        }
        standings.sort(Comparator.comparingInt(BiathlonStandings.BiathlonAthlete::getTotalTime)
                .thenComparing(BiathlonStandings.BiathlonAthlete::getSkiTimeResult)
                .thenComparingInt(BiathlonStandings.BiathlonAthlete::getAthleteNumber));
        return standings;
    }
    public static Map<Ranking, BiathlonAthlete> getPodium(List<BiathlonAthlete> standings) {
        Map<Ranking, BiathlonAthlete> podium = new LinkedHashMap<>();
        podium.put(Ranking.WINNER, standings.get(0));
        podium.put(Ranking.RUNNER_UP, standings.get(1));
        podium.put(Ranking.THIRD_PLACE, standings.get(2));
        return podium;
    }
    static int calculatePenalty(String shootingRange) {
        int missedShots = 0;
        for (char shotResult : shootingRange.toCharArray()) {
            if (shotResult == 'o') {
                missedShots++;
            }
        }
        return missedShots * 10;
    }
    static int convertTimeToSeconds(String time) {
        String[] parts = time.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        return minutes * 60 + seconds;
    }
    public enum Ranking {
        WINNER,
        RUNNER_UP,
        THIRD_PLACE
    }
    public static class BiathlonAthlete {
        private int athleteNumber;
        private String athleteName;
        private String countryCode;
        private String skiTimeResult;
        private String firstShootingRange;
        private String secondShootingRange;
        private String thirdShootingRange;
        private int totalTime;
        public BiathlonAthlete(int athleteNumber, String athleteName, String countryCode, String skiTimeResult,
                               String firstShootingRange, String secondShootingRange, String thirdShootingRange) {
            this.athleteNumber = athleteNumber;
            this.athleteName = athleteName;
            this.countryCode = countryCode;
            this.skiTimeResult = skiTimeResult;
            this.firstShootingRange = firstShootingRange;
            this.secondShootingRange = secondShootingRange;
            this.thirdShootingRange = thirdShootingRange;
        }
        public int getAthleteNumber() {
            return athleteNumber;
        }
        public String getAthleteName() {
            return athleteName;
        }
        public String getCountryCode() {
            return countryCode;
        }
        public String getSkiTimeResult() {
            return skiTimeResult;
        }
        public String getFirstShootingRange() {
            return firstShootingRange;
        }
        public String getSecondShootingRange() {
            return secondShootingRange;
        }
        public String getThirdShootingRange() {
            return thirdShootingRange;
        }
        public int getTotalTime() {
            return totalTime;
        }
        public void setTotalTime(int totalTime) {
            this.totalTime = totalTime;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            BiathlonAthlete other = (BiathlonAthlete) obj;
            return athleteNumber == other.athleteNumber
                    && Objects.equals(athleteName, other.athleteName)
                    && Objects.equals(countryCode, other.countryCode)
                    && Objects.equals(skiTimeResult, other.skiTimeResult)
                    && Objects.equals(firstShootingRange, other.firstShootingRange)
                    && Objects.equals(secondShootingRange, other.secondShootingRange)
                    && Objects.equals(thirdShootingRange, other.thirdShootingRange);
        }
        @Override
        public int hashCode() {
            return Objects.hash(athleteNumber, athleteName, countryCode, skiTimeResult,
                    firstShootingRange, secondShootingRange, thirdShootingRange);
        }
        @Override
        public String toString() {
            int minutes = totalTime / 60;
            int seconds = totalTime % 60;
            return athleteName + " " + String.format("%02d:%02d", minutes, seconds);
        }
    }
}


