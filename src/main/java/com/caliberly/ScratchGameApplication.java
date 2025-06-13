package com.caliberly;

import com.caliberly.models.Config;
import com.caliberly.models.GameResult;
import com.caliberly.service.GameEngine;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ScratchGameApplication {

    public static void main(String[] args) throws Exception {

        Logger logger = LoggerFactory.getLogger(ScratchGameApplication.class);
        String configFile = null;
        int betAmount = 0;

        logger.info("This is a test log");

        for (int i = 0; i < args.length; i++) {
            if ("--config".equals(args[i])) {
                configFile = args[++i];
            }
            if ("--betting-amount".equals(args[i])) {
                betAmount = Integer.parseInt(args[++i]);
            }
        }

        if (configFile == null || betAmount <= 0) {
            System.err.println("Usage: java -jar build/libs/ScratchGame.jar --config config.json --betting-amount 100");
            System.exit(1);
        }

        // Convert the Config.json file to an Object/Class for easy manipulation
        ObjectMapper mapper = new ObjectMapper();
        Config config = mapper.readValue(new File(configFile), Config.class);

        GameEngine engine = new GameEngine(config);
        GameResult result = engine.play(betAmount);
        // Using Pretty form ObjectMapper to have a nice Json for output
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));

    }

}
