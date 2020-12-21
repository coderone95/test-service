package com.coderone95.test.service.model;

public class Teams {

    private String team_name;
    private Long wins;
    private Long losses;
    private Long ties;
    private Long score;

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public Long getWins() {
        return wins;
    }

    public void setWins(Long wins) {
        this.wins = wins;
    }

    public Long getLosses() {
        return losses;
    }

    public void setLosses(Long losses) {
        this.losses = losses;
    }

    public Long getTies() {
        return ties;
    }

    public void setTies(Long ties) {
        this.ties = ties;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }
}
