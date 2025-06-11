package com.caliberly.models;

import java.util.List;
import java.util.Map;

public class GameResult {
    public String[][] matrix;
    public int reward;
    public Map<String, List<String>> applied_winning_combinations;
    public String applied_bonus_symbol;

    public GameResult(String[][] matrix, int reward,
                      Map<String, List<String>> appliedWins, String bonus) {
        this.matrix = matrix;
        this.reward = reward;
        this.applied_winning_combinations = appliedWins.isEmpty() ? null : appliedWins;
        this.applied_bonus_symbol = bonus;
    }
}
