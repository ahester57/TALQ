package edu.umsl.hester.superclickers.userdata;

import java.util.ArrayList;

/**
 *
 *
 */

public class Stats {

    private ArrayList<Integer> statsList;

    public Stats() {
        this.statsList = new ArrayList<>();
    }

    public ArrayList<Integer> getStatsList() {
        return this.statsList;
    }

    public void setStatsList(int aPoints, int bPoints, int cPoints, int dPoints) {
        statsList.add(aPoints);
        statsList.add(bPoints);
        statsList.add(cPoints);
        statsList.add(dPoints);
    }

}
