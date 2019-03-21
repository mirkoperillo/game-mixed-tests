package eu.trentorise.game.test;

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
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;

public class KidsGoGreenGameTest extends GameTest {

    private static final String GAME_ID = "KidsGoGreen";

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private GameService gameSrv;

    @Autowired
    private MongoTemplate mongo;

    @Override
    public void initEnv() {
        TeamState team = new TeamState(GAME_ID, "Scuola Schmid 2A-B-C");
        team.setName("Scuola Schmid 4B 4C");
        team.setMembers(Arrays.asList("classe"));
        playerSrv.saveTeam(team);

        team = new TeamState(GAME_ID, "classe");
        team.setName("classe");
        team.setMembers(Arrays.asList("alunno1"));
        playerSrv.saveTeam(team);


    }


    private PointConcept attachPeriods(PointConcept pc) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Date start = Date.from(LocalDate.parse("26/02/2018", formatter)
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


        defineGameHelper(null, GAME_ID, actions, concepts);


        String rulesFolderPath =
                "/home/mirko/data/git/game-engine-climb.rules/src/main/resources/rules-Schmid-2A-B-C/";
        loadFilesystemRules(GAME_ID, rulesFolderPath, false);

    }

    @Override
    public void defineExecData(List<ExecData> execList) {

        // pedibus action
        Map<String, Object> payload = null;
        ExecData input = null;


        // gain last badge an use challenge prize
        payload = new HashMap<>();
        // payload.put("meters-distance", 11921000d);
        payload.put("meters-distance", 3700000.0);
        input = new ExecData(GAME_ID, "PedibusKidTrip", "alunno1", payload);
        execList.add(input);

        // caso in cui scuola arrivi a badge precedente a quello di sfida
        // e caso in cui vincono sfida
    }

    @Override
    public void analyzeResult() {
        // TODO Auto-generated method stub

    }

}
