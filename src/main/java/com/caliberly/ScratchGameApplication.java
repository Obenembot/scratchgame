package com.caliberly;


import com.caliberly.models.Config;
import com.caliberly.models.GameResult;
import com.caliberly.service.GameEngine;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

//@SpringBootApplication
public class ScratchGameApplication {

    public static void main(String[] args) throws Exception {
//        SpringApplication.run(ScratchGameApplication.class, args);
        String configFile = null;
        int betAmount = 0;

        for (int i = 0; i < args.length; i++) {
            if ("--config".equals(args[i])) configFile = args[++i];
            if ("--betting-amount".equals(args[i])) betAmount = Integer.parseInt(args[++i]);
        }

        if (configFile == null || betAmount <= 0) {
            System.err.println("Usage: java -jar game.jar --config config.json --betting-amount 100");
            System.exit(1);
        }

        ObjectMapper mapper = new ObjectMapper();
        Config config = mapper.readValue(new File(configFile), Config.class);

        GameEngine engine = new GameEngine(config);
        GameResult result = engine.play(betAmount);

        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));

    }

}
