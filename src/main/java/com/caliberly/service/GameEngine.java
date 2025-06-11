package com.caliberly.service;

import com.caliberly.models.Config;
import com.caliberly.models.GameResult;

import java.util.*;
import java.util.stream.Collectors;

public class GameEngine {
    private final Config config;
    private final Random random = new Random();

    public GameEngine(Config config) {
        this.config = config;
    }

    public GameResult play(int betAmount) {
        String[][] matrix = new String[config.rows][config.columns];
        Map<String, List<String>> appliedWins = new HashMap<>();
        Set<String> foundSymbols = new HashSet<>();
        double totalReward = 0;
        String bonusApplied = "";

        // 1. Fill matrix with standard & bonus symbols based on probabilities
        for (int row = 0; row < config.rows; row++) {
            for (int col = 0; col < config.columns; col++) {
                matrix[row][col] = drawSymbol(row, col);
                if (isStandard(matrix[row][col])) foundSymbols.add(matrix[row][col]);
            }
        }

        // 2. Detect winning combinations
        for (String symbol : foundSymbols) {
            List<String> wins = new ArrayList<>();
            long count = Arrays.stream(matrix)
                    .flatMap(Arrays::stream)
                    .filter(s -> s.equals(symbol)).count();

            for (Map.Entry<String, Config.WinCombination> entry : config.win_combinations.entrySet()) {
                Config.WinCombination wc = entry.getValue();
                if ("same_symbols".equals(wc.when) && count >= wc.count) {
                    wins.add(entry.getKey());
                } else if ("linear_symbols".equals(wc.when) && matchesLinear(matrix, symbol, wc.covered_areas)) {
                    wins.add(entry.getKey());
                }
            }

            if (!wins.isEmpty()) {
                appliedWins.put(symbol, wins);
                double rewardMultiplier = wins.stream()
                        .map(w -> config.win_combinations.get(w).reward_multiplier)
                        .reduce(1.0, (a, b) -> a * b);
                totalReward += betAmount * config.symbols.get(symbol).reward_multiplier * rewardMultiplier;
            }
        }

//         3. Apply bonus if applicable
        String bonus = pickBonus(matrix);
        if (bonus != null && !"MISS".equals(bonus) && !appliedWins.isEmpty()) {
            bonusApplied = bonus;
            Config.Symbol bonusSymbol = config.symbols.get(bonus);
            switch (bonusSymbol.impact) {
                case "multiply_reward" -> totalReward *= bonusSymbol.reward_multiplier;
                case "extra_bonus" -> totalReward += bonusSymbol.extra;
            }
        }

        return new GameResult(matrix, (int) totalReward, appliedWins, bonusApplied);
    }

    private String pickBonus(String[][] matrix) {
        Set<String> bonusSymbolsInMatrix = new HashSet<>();

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                String symbol = matrix[row][col];
                Config.Symbol configSymbol = config.symbols.get(symbol);
                if (configSymbol != null && "bonus".equals(configSymbol.type)) {
                    bonusSymbolsInMatrix.add(symbol);
                }
            }
        }

        if (bonusSymbolsInMatrix.isEmpty()) {
            return null;
        }

        int index = random.nextInt(bonusSymbolsInMatrix.size());
        return bonusSymbolsInMatrix.stream().skip(index).findFirst().orElse(null);
    }

    private String drawSymbol(int row, int col) {
        Optional<Config.StandardSymbolProbability> probOpt = config.probabilities.standard_symbols.stream()
                .filter(p -> p.row == row && p.column == col).findFirst();

        Map<String, Integer> probs = probOpt.map(p -> p.symbols)
                .orElse(config.probabilities.standard_symbols.get(0).symbols);

        String standardSymbol = weightedRandom(probs);
        if (random.nextDouble() < 0.2) {
            return weightedRandom(config.probabilities.bonus_symbols.symbols);
        }

        return standardSymbol;
    }

    private boolean isStandard(String symbol) {
        return "standard".equals(config.symbols.get(symbol).type);
    }

    private String weightedRandom(Map<String, Integer> map) {
        int sum = map.values().stream().mapToInt(i -> i).sum();
        int r = random.nextInt(sum) + 1;
        int cumulative = 0;
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            cumulative += e.getValue();
            if (r <= cumulative) return e.getKey();
        }
        return map.keySet().iterator().next(); // fallback
    }

    private boolean matchesLinear(String[][] matrix, String symbol, List<List<String>> areas) {
        if (areas == null) return false;
        for (List<String> group : areas) {
            boolean match = true;
            for (String coord : group) {
                String[] parts = coord.split(":");
                if (!matrix[Integer.parseInt(parts[0])][Integer.parseInt(parts[1])].equals(symbol)) {
                    match = false;
                    break;
                }
            }
            if (match) return true;
        }
        return false;
    }
}
