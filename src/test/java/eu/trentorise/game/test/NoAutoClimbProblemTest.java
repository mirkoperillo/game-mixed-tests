package eu.trentorise.game.test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import eu.trentorise.game.model.ChallengeModel;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;

/*
 * Verify if this test is still useful
 */

public class NoAutoClimbProblemTest extends GameTest {

    private static final String GAME_ID = "Climb";
    private static final String DOMAIN = "";

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private GameService gameSrv;

    @Autowired
    private MongoTemplate mongo;

    @Override
    public void initEnv() {
        TeamState team = new TeamState(GAME_ID, "scuola");
        team.setName("scuola");
        team.setMembers(Arrays.asList("classe"));
        playerSrv.saveTeam(team);

        team = new TeamState(GAME_ID, "classe");
        team.setName("classe");
        team.setMembers(Arrays.asList("alunno1", "alunno2"));
        playerSrv.saveTeam(team);


        // Map<String, Object> data = new HashMap<>();
        // data.put("bonusScore", 850000d);
        // data.put("legName", "Islanda");
        // data.put("VirtualPrize", "Mongolfiera da Islanda a Fiordi Norvegesi");
        // data.put("bonusPointType", "bonus_distance");
        // data.put("prizeWon", false);
        // playerSrv.assignChallenge(GAME_ID, "scuola", new ChallengeAssignment("ScuolaSenzAuto",
        // "test-challenge-senz-auto", data, null, null, null));

    }


    private PointConcept attachPeriods(PointConcept pc) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Date start = Date.from(LocalDate.parse("16/10/2017", formatter)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
        long DAY = 60 * 60 * 24 * 1000;
        long WEEK = DAY * 7;

        pc.addPeriod("weekly", start, WEEK);
        pc.addPeriod("daily", start, DAY);

        return pc;
    }

    @Override
    public void defineGame() {

        mongo.getDb().drop();

        List<String> actions = Arrays.asList("PedibusKidTrip", "CalendarDayFilled",
                "PlayerCalendarTrip", "ClassWalkTrip");


        List<GameConcept> concepts = new ArrayList<>();
        concepts.add(new BadgeCollectionConceptBuilder().setName("LegsToMinsk").build());

        List<String> scoreNames = Arrays.asList("zeroImpact_wAdult_distance", "total_trips",
                "zeroImpact_solo_distance", "car_distance", "bonus_distance", "total_distance",
                "group_trips", "pandr_distance", "bus_distance", "pandr_trips", "bus_trips",
                "car_trips", "zeroImpact_wAdult_trips", "pedibus_distance", "no_car_classes",
                "absence_count", "participation_count", "pedibus_trips", "zeroImpact_solo_trips");

        for (String name : scoreNames) {
            PointConcept pc = new PointConcept(name);
            pc = attachPeriods(pc);
            concepts.add(pc);
        }


        defineGameHelper(DOMAIN, GAME_ID, actions, concepts);


        // Challenge
        ChallengeModel senzAuto = new ChallengeModel();
        senzAuto.setGameId(GAME_ID);
        senzAuto.setName("ScuolaSenzAuto");
        Set<String> vars = new HashSet<>();
        vars.add("bonusScore");
        vars.add("legName");
        vars.add("VirtualPrize");
        vars.add("bonusPointType");
        vars.add("prizeWon");

        senzAuto.setVariables(vars);
        gameSrv.saveChallengeModel(GAME_ID, senzAuto);

        loadClasspathRules(GAME_ID,
                Arrays.asList("rules/climb/calendartrips.drl", "rules/climb/classday.drl",
                        "rules/climb/classtrips.drl", "rules/climb/constants",
                        "rules/climb/legsbadges.drl", "rules/climb/pedibus.drl",
                        "rules/climb/weeklytasks.drl",
                        "rules/climb/challenges/ChallengeScuolaSenzAuto.drl"));
    }

    @Override
    public void defineExecData(List<ExecData> execList) {

        // pedibus action
        Map<String, Object> payload = null;
        ExecData input = null;

        payload = new HashMap<>();
        payload.put("meteo", "sunny");
        payload.put("mode", "bus");
        payload.put("school-date", 1553774624007L);
        input = new ExecData(GAME_ID, "PlayerCalendarTrip", "alunno1", payload);
        execList.add(input);

        payload = new HashMap<>();
        payload.put("meteo", "sunny");
        payload.put("mode", "walk");
        payload.put("school-date", 1553348843000L);
        input = new ExecData(GAME_ID, "PlayerCalendarTrip", "alunno2", payload);
        execList.add(input);

    }

    @Override
    public void analyzeResult() {
        // assertionPoint(GAME_ID, 1.0, "alunno1", "participation_count");
        // assertionPoint(GAME_ID, 1.0, "classe", "participation_count");
        // assertionPoint(GAME_ID, 1.0, "scuola", "participation_count");

    }

}
