package eu.trentorise.game.test;

import static org.junit.Assert.fail;

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

import eu.trentorise.game.model.ChallengeModel;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;

public class ClimbTestGameTest extends GameTest {

    private static final String GAME_ID = "climb-test";
    private static final String DOMAIN = "";

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private GameService gameSrv;

    @Autowired
    private MongoTemplate mongo;

    @Override
    public void initEnv() {
        TeamState team = new TeamState(GAME_ID, "scuolatest");
        team.setName("scuolatest");
        team.setMembers(Arrays.asList("classe"));
        playerSrv.saveTeam(team);

        team = new TeamState(GAME_ID, "classe");
        team.setName("classe");
        playerSrv.saveTeam(team);


        Map<String, Object> data = new HashMap<>();

        // data.put("bonusScore", 850_000d);
        // data.put("VirtualPrize", "Mongolfiera da Islanda a Fiordi Norvegesi");
        // data.put("bonusPointType", "bonus_distance");
        // data.put("prizeWon", false);
        // playerSrv.assignChallenge(GAME_ID, "scuola", new ChallengeAssignment("ScuolaSenzAuto",
        // "test-challenge-senz-auto", data, null, null, null));

        // data.put("bonusScore", 1_000d);
        // data.put("VirtualPrize", "Mongolfiera da Islanda a Fiordi Norvegesi");
        // data.put("bonusPointType", "bonus_distance");
        // data.put("_target_numTrips_", 2.0);
        // data.put("prizeWon", false);
        // playerSrv.assignChallenge(GAME_ID, "scuola", new ChallengeAssignment("ViaggiGiornalieri",
        // "test-challenge-viaggi", data, null, null, null));
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
        concepts.add(new BadgeCollectionConceptBuilder().setName("LegsCollection").build());

        List<String> scoreNames = Arrays.asList("total_trips", "total_distance",
                "walk_distance", "bike_distance",
                "car_distance", "carpooling_distance",
                "bonus_distance", "pandr_distance", "bus_distance", "pedibus_distance",
                "no_car_classes", "absence_count", "pedibus_trips",
                "pandr_trips", "bus_trips", "car_trips", "walk_trips", "bike_trips",
                "carpooling_trips", "group_trips");

        for (String name : scoreNames) {
            PointConcept pc = new PointConcept(name);
            pc = attachPeriods(pc);
            concepts.add(pc);
        }


        defineGameHelper(DOMAIN, GAME_ID, actions, concepts);


        // Challenge SenzAuto
        ChallengeModel senzAuto = new ChallengeModel();
        senzAuto.setGameId(GAME_ID);
        senzAuto.setName("ScuolaSenzAuto");
        senzAuto.getVariables().add("bonusScore");
        senzAuto.getVariables().add("legName");
        senzAuto.getVariables().add("VirtualPrize");
        senzAuto.getVariables().add("bonusPointType");
        senzAuto.getVariables().add("prizeWon");

        gameSrv.saveChallengeModel(GAME_ID, senzAuto);

        // Challenge ViaggiGiornalieriZeroImpact
        ChallengeModel viaggiGiornalieri = new ChallengeModel();
        viaggiGiornalieri.setGameId(GAME_ID);
        viaggiGiornalieri.setName("ViaggiGiornalieri");
        viaggiGiornalieri.getVariables().add("bonusScore");
        viaggiGiornalieri.getVariables().add("legName");
        viaggiGiornalieri.getVariables().add("VirtualPrize");
        viaggiGiornalieri.getVariables().add("bonusPointType");
        viaggiGiornalieri.getVariables().add("prizeWon");
        viaggiGiornalieri.getVariables().add("_target_numTrips_");

        gameSrv.saveChallengeModel(GAME_ID, viaggiGiornalieri);

        try {
            loadClasspathRules(GAME_ID, "rules/" + GAME_ID);
        } catch (IOException e) {
            fail("Exception loading rules");
        }
    }

    @Override
    public void defineExecData(List<ExecData> execList) {

        // : ClassWalkTrip, playerId: 3A, executionMoment: 25-03-2020 16:28:20, data:
        // {class-distance=3000.0, meteo=sunny, participants=9.0, school-date=1585090800000},
        // factObjs: null

        Map<String, Object> payload = null;
        ExecData input = null;
        payload = new HashMap<>();
        payload.put("class-distance", 3000d);
        payload.put("participants", 9d);
        payload.put("school-date", 1585090800000L);
        payload.put("meteo", "sunny");
        input = new ExecData(GAME_ID, "ClassWalkTrip", "classe", payload);
        execList.add(input);
        




    }

    @Override
    public void analyzeResult() {
    }

}
