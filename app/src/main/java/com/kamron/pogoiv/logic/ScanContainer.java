package com.kamron.pogoiv.logic;

import java.util.ArrayList;

/**
 * Created by Johan on 2016-08-19.
 * <p/>
 * A class which keeps the 2 most recent IV scans in memory
 */
public class ScanContainer {
    public static final ScanContainer scanContainer = new ScanContainer();
    public IVScanResult oneScanAgo = null;
    public IVScanResult twoScanAgo = null;

    /**
     * pushes the 3 scan ago out of memory, and remembers the two latest scanns
     *
     * @param res
     */
    private void addNewScan(IVScanResult res) {
        twoScanAgo = oneScanAgo;
        oneScanAgo = res;
    }

    /**
     * Create a new IVScanResult and updates the scanContainer singleton.
     * @param pokemon
     * @param estimatedPokemonLevel
     * @param pokemonCP
     * @param b
     * @return
     */
    public static IVScanResult createIVScanResult(Pokemon pokemon, double estimatedPokemonLevel, int pokemonCP,
                                                  boolean b) {
        IVScanResult res = new IVScanResult(pokemon, estimatedPokemonLevel, pokemonCP, b);
        scanContainer.addNewScan(res);
        return res;
    }


    /**
     * Compares two pokemon scan results, and returns a list of which ivs the scans have in commomn
     * Useful when you power up a pokemon, and wanna see which combinations you can trash
     *
     * @param poke1 the first pokemon scan
     * @param poke2 the second pokemon scan
     * @return ArrayList of ivcombination that are present in both iv scans.
     */
    public static ArrayList<IVCombination> findIVIntersection(IVScanResult poke1, IVScanResult poke2) {
        ArrayList<IVCombination> intersection = new ArrayList<>();


        if (poke1 != null && poke2 != null) {
            ArrayList<IVCombination> p1IVs = poke1.iVCombinations;
            ArrayList<IVCombination> p2IVs = poke2.iVCombinations;
            for (IVCombination p1IV : p1IVs) {
                for (IVCombination p2IV : p2IVs) {
                    if (p1IV.equals(p2IV)) {
                        intersection.add(p1IV);
                    }
                }
            }
        }

        return intersection;
    }

    /**
     * Checks if the previous scanned pokemon can be the same pokemon as the one scanned 2 scans ago
     * checks if newer scan has higher level, and same or next evolution. (because pokemon cant de-level or devolve)
     *
     * @return true if the pokemon can be same
     */
    public boolean canLastScanBePoweredUpPreviousScan() {

        IVScanResult p1scan = oneScanAgo;
        IVScanResult p2scan = twoScanAgo;
        if (p1scan != null && p2scan != null) {
            Pokemon p1 = oneScanAgo.pokemon;
            Pokemon p2 = twoScanAgo.pokemon;

            boolean pokemonHasLeveledUp = p1scan.estimatedPokemonLevel > p2scan.estimatedPokemonLevel;
            boolean isEvolved = p1.isInNextEvolution(p2);

            boolean somethingImproved = pokemonHasLeveledUp || isEvolved;
            boolean isSameOrHigherLevel = p1scan.estimatedPokemonLevel >= p2scan.estimatedPokemonLevel;
            boolean isSameOrHigherEvolution = p1.number == p2.number || p1.isInNextEvolution(p2);
            return somethingImproved && isSameOrHigherLevel && isSameOrHigherEvolution;
        }

        return false;
    }

    /**
     * Compares the latest two pokemon scan results, and returns a list of which ivs the scans have in commomn
     * Useful when you power up a pokemon, and wanna see which combinations you can trash
     *
     * @return ArrayList of ivcombination that are present in both iv scans.
     */
    public ArrayList<IVCombination> getLatestIVIntersection() {
        return findIVIntersection(oneScanAgo, twoScanAgo);

    }

    /**
     * returns a string which is either the name of the previously scanned pokemon, or ""
     *
     * @return
     */
    public String getPrevScanName() {
        if (twoScanAgo != null) {
            return twoScanAgo.pokemon.name;
        } else {
            return "";
        }

    }
}
