package com.mom.soccer.dataDto;

/**
 * Created by sungbo on 2016-07-22.
 */
public class RankingVo {

    private int    ranking;
    private String number;
    private String userimage;
    private String username;
    private String teamname;
    private String level;
    private String userscore;
    private String medalname;

    public RankingVo(){}

    public RankingVo(int ranking, String number, String userimage, String username, String teamname, String level, String userscore, String medalname) {
        this.ranking = ranking;
        this.number = number;
        this.userimage = userimage;
        this.username = username;
        this.teamname = teamname;
        this.level = level;
        this.userscore = userscore;
        this.medalname = medalname;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUserscore() {
        return userscore;
    }

    public void setUserscore(String userscore) {
        this.userscore = userscore;
    }

    public String getMedalname() {
        return medalname;
    }

    public void setMedalname(String medalname) {
        this.medalname = medalname;
    }

    @Override
    public String toString() {
        return "RankingVo{" +
                "ranking=" + ranking +
                ", number='" + number + '\'' +
                ", userimage='" + userimage + '\'' +
                ", username='" + username + '\'' +
                ", teamname='" + teamname + '\'' +
                ", level='" + level + '\'' +
                ", userscore='" + userscore + '\'' +
                ", medalname='" + medalname + '\'' +
                '}';
    }
}
