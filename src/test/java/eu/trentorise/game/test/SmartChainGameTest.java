package eu.trentorise.game.test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import eu.trentorise.game.model.CustomData;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.PlayerService;

public class SmartChainGameTest extends GameTest {

    private static final String GAME_ID = "smart-chain";
    private static final String DOMAIN = "";

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private MongoTemplate mongo;

    @Override
    public void initEnv() {

        Map<String, Object> customData = new CustomData();
        customData.put("components", 4);
        PointConcept territory = new PointConcept("character_territory");
        territory.setScore(100d);
        PointConcept culture = new PointConcept("character_culture");
        culture.setScore(100d);
        PointConcept sport = new PointConcept("character_sport");
        sport.setScore(100d);
        savePlayerState(GAME_ID, "eddie", Arrays.asList(territory, culture, sport), customData);
    }


    private PointConcept attachPeriods(PointConcept pc) {
        pc.addPeriod("daily", new org.joda.time.LocalDate().toDate(), 24 * 60 * 60 * 1000);
        return pc;
    }

    private Date date(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return Date.from(LocalDate.parse(dateString, formatter)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public void defineGame() {

        mongo.getDb().drop();

        List<String> actions = Arrays.asList("bring-a-friend", "complete-survey",
                "spend-another-night", "spend-50", "use-public-transportation", "experience",
                "consume-character", "consume-reward");

        List<GameConcept> concepts = new ArrayList<>();

        List<String> scoreNames =
                Arrays.asList("character_culture", "character_territory", "character_sport",
                        "total_culture", "total_territory", "total_sport", "reward_culture",
                        "reward_territory", "reward_sport");

        for (String name : scoreNames) {
            PointConcept pc = new PointConcept(name);
            pc = attachPeriods(pc);
            concepts.add(pc);
        }

        defineGameHelper(DOMAIN, GAME_ID, actions, concepts);

        try {
            loadClasspathRules(GAME_ID, "rules/" + GAME_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void defineExecData(List<ExecData> execList) {
        Map<String, Object> data = null;
        ExecData action = null;

        action = new ExecData(GAME_ID, "bring-a-friend", "eddie", null);

        // action = new ExecData(GAME_ID, "complete-survey", "eddie", null);

        // data = new HashMap<>();
        // data.put("value", 100.0);
        // action = new ExecData(GAME_ID, "spend-another-night", "eddie", data);

        // data = new HashMap<>();
        // data.put("name", "centro-documentazione-luserna");
        // action = new ExecData(GAME_ID, "experience", "eddie", data);

        data = new HashMap<>();
        data.put("territory", 50d);
        data.put("culture", 5d);
        data.put("sport", 5d);
        data.put("character", "lupo");
        action = new ExecData(GAME_ID, "consume-character", "eddie", data);

        // data = new HashMap<>();
        // data.put("distanceKm", 5.0);
        // action = new ExecData(GAME_ID, "use-public-transportation", "eddie", data);

        execList.add(action);

    }

    @Override
    public void analyzeResult() {
        assertionPoint(GAME_ID, 50d + 10d, "eddie", "character_territory");
        assertionPoint(GAME_ID, 95d, "eddie", "character_culture");
        assertionPoint(GAME_ID, 95d, "eddie", "character_sport");

        // assertionPoint(GAME_ID, 50d, "eddie", "territory");
        // assertionPoint(GAME_ID, 3d, "eddie", "territory");

        // assertionPoint(GAME_ID, 14.0, "eddie", "territory");

        // assertionPoint(GAME_ID, 0.5, "eddie", "territory");
    }

}
