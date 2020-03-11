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

import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.PointConceptStateHelperFactory;
import eu.trentorise.game.model.PointConceptStateHelperFactory.PointConceptStateHelper;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.PlayerService;

public class InnoweeeGameTest extends GameTest {

    private static final String GAME_ID = "innoweee";
    private static final String DOMAIN = "";

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private MongoTemplate mongo;

    @Autowired
    private PointConceptStateHelperFactory helperFactory;

    @Override
    public void initEnv() {
        TeamState team = new TeamState(GAME_ID, "scuola");
        team.setName("scuola");
        team.setMembers(Arrays.asList("classe"));
        playerSrv.saveTeam(team);

        List<GameConcept> scores = new ArrayList<>();

        PointConceptStateHelper h = helperFactory.instanceOf(GAME_ID, "reduceCoin");
        h.setScoreInTime(new Date(), 11d);
        scores.add(h.build());

        h = helperFactory.instanceOf(GAME_ID, "totalReduce");
        h.setScoreInTime(new Date(), 11d);
        scores.add(h.build());

        h = helperFactory.instanceOf(GAME_ID, "totalReduce");
        h.setScoreInTime(new Date(), 11d);
        scores.add(h.build());

        h = helperFactory.instanceOf(GAME_ID, "reuseCoin");
        h.setScoreInTime(new Date(), 1d);
        scores.add(h.build());
        h = helperFactory.instanceOf(GAME_ID, "totalReuse");
        h.setScoreInTime(new Date(), 1d);
        scores.add(h.build());

        h = helperFactory.instanceOf(GAME_ID, "recycleCoin");
        h.setScoreInTime(new Date(), 5d);
        scores.add(h.build());
        h = helperFactory.instanceOf(GAME_ID, "totalRecycle");
        h.setScoreInTime(new Date(), 5d);
        scores.add(h.build());

        savePlayerState(GAME_ID, "classe", scores);
    }


    private PointConcept attachPeriods(PointConcept pc) {
        /**
         * ACTUALLY this addPeriod signature is supported only by game-engine on master branch
         */
        // PROD pc.addPeriod("R1", date("02/12/2019"), date("07/12/2019"), -1);
        pc.addPeriod("R1", date("29/02/2020"), date("07/03/2020"), -1);
        pc.addPeriod("R2", date("07/03/2020"), date("16/03/2020"), -1);
        pc.addPeriod("R3", date("16/03/2020"), date("23/03/2020"), -1);
        pc.addPeriod("R4", date("23/03/2020"), date("30/03/2020"), -1);
        pc.addPeriod("R5", date("30/03/2020"), date("06/04/2020"), -1);
        pc.addPeriod("R6", date("06/04/2020"), date("18/04/2020"), -1);

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

        List<String> actions =
                Arrays.asList("itemDelivery", "reduceReport", "buildRobot", "donation");

        List<GameConcept> concepts = new ArrayList<>();

        List<String> scoreNames =
                Arrays.asList("reduceCoin", "reuseCoin", "recycleCoin", "totalItems", "totalReuse",
                        "totalRecycle", "totalReduce", "weight", "CO2", "plastic", "glass", "iron",
                        "aluminium", "copper", "tin", "nickel", "silver", "gold", "platinum");

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
        data = new HashMap<String, Object>();
        data.put("reduceCoin", 0d);
        data.put("reuseCoin", -10d);
        data.put("recycleCoin", 5d);
        ExecData build = new ExecData(GAME_ID, "donation", "classe", data);
        execList.add(build);

        // Date dateR2 = date("22/01/2019");
        // data = new HashMap<String, Object>();
        // data.put("reduceCoin", 1d);
        // data.put("reuseCoin", 0d);
        // data.put("recycleCoin", 3d);
        // ExecData build = new ExecData(GAME_ID, "buildRobot", "classe", data, dateR2);
        // execList.add(build);

        // Date dateR1 = date("02/03/2019");
        // data = new HashMap<String, Object>();
        // data.put("reduceCoin", 5d);
        // ExecData reduceReport = new ExecData(GAME_ID, "reduceReport", "classe", data, dateR1);
        // execList.add(reduceReport);


        // Date dateR3 = date("25/01/2019");
        // data = new HashMap<String, Object>();
        // data.put("weee", false);
        // data.put("weight", 5d);
        // data.put("plastic", 2d);
        // data.put("glass", 1d);
        // data.put("iron", 1d);
        // data.put("aluminium", 3d);
        // data.put("copper", 5d);
        // data.put("tin", 6d);
        // data.put("nickel", 7d);
        // data.put("silver", 8d);
        // data.put("gold", 8d);
        // data.put("platinum", 3d);
        /**
         * ACTUALLY supported only by game-engine on master branch
         */
        // ExecData itemDelivery = new ExecData(GAME_ID, "itemDelivery", "classe", data, dateR3);
        // execList.add(itemDelivery);

    }

    @Override
    public void analyzeResult() {

    }

}
