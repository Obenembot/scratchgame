package com.caliberly.models;

import java.util.List;
import java.util.Map;

public class Config {
    public int columns;
    public int rows;
    public Map<String, Symbol> symbols;
    public Probabilities probabilities;
    public Map<String, WinCombination> win_combinations;

    public static class Symbol {
        public String type;
        public Double reward_multiplier;
        public Integer extra;
        public String impact;
    }

    public static class Probabilities {
        public List<StandardSymbolProbability> standard_symbols;
        public BonusSymbolProbability bonus_symbols;
    }

    public static class StandardSymbolProbability {
        public int column;
        public int row;
        public Map<String, Integer> symbols;
    }

    public static class BonusSymbolProbability {
        public Map<String, Integer> symbols;
    }

    public static class WinCombination {
        public double reward_multiplier;
        public String when;
        public String group;
        public Integer count;
        public List<List<String>> covered_areas;
    }
}
