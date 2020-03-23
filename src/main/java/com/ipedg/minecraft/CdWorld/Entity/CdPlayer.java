package com.ipedg.minecraft.CdWorld.Entity;

import java.util.Objects;

public class CdPlayer {
    private long useTime;
    private long CDTime;
    private String playername;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CdPlayer)){
            return false;
        }
        CdPlayer obj1 = (CdPlayer) o;
        return obj1.playername.equalsIgnoreCase(this.playername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playername);
    }

    @Override
    public String toString() {
        return "CdPlayer{" +
                "useTime=" + useTime +
                ", CDTime=" + CDTime +
                ", playername='" + playername + '\'' +
                '}';
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }

    public long getCDTime() {
        return CDTime;
    }

    public void setCDTime(long CDTime) {
        this.CDTime = CDTime;
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public CdPlayer(long useTime, long CDTime, String playername) {
        this.useTime = useTime;
        this.CDTime = CDTime;
        this.playername = playername;
    }
}
