package eu.trentorise.game.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;

import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.PlayerService;

public class PlayAndGo2019MaxDailyTest extends GameTest {

    @Autowired
    PlayerService playerSrv;

    private static final String GAME = "playAndGo2019";
    private static final String ACTION = "save_itinerary";
    private static final String DOMAIN = "";

    @Override
    public void initEnv() {

    }

    @Override
    public void defineGame() {
        List<String> scoreNames = Arrays.asList("green leaves", "PandR_Trips", "Transit_Trips",
                "BikeSharing_Km", "Bike_Trips", "Walk_Trips", "Walk_Km", "Car_Trips", "NoCar_Trips",
                "Bus_Km", "BikeSharing_Trips", "Car_Km", "Train_Km", "Recommendations", "Bus_Trips",
                "Train_Trips", "Bike_Km", "ZeroImpact_Trips");


        final Date startDate = LocalDate.now().toDate();
        final long dayDurationInMillis = 24 * 60 * 60 * 1000;
        List<GameConcept> concepts = scoreNames.stream().map(s -> {
            PointConcept p = new PointConcept(s);
            p.addPeriod("weekly", startDate, dayDurationInMillis);
            p.addPeriod("daily", startDate, dayDurationInMillis);
            return p;
        }).collect(Collectors.toList());

        concepts.add(new BadgeCollectionConcept("green leaves"));

        defineGameHelper(DOMAIN, GAME, Arrays.asList(ACTION), concepts);

        try {
            loadClasspathRules(GAME, "rules/" + GAME);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void defineExecData(List<ExecData> execList) {
        Map<String, Object> data = new HashMap<String, Object>();
        ExecData input = null;
        data.put("busDistance", 6.69); // 33 gl
        input = new ExecData(GAME, ACTION, "prowler", data);
        execList.add(input);

        data = new HashMap<String, Object>(); // 2 gl
        data.put("busDistance", 0.386);
        input = new ExecData(GAME, ACTION, "prowler", data);
        execList.add(input);

        data = new HashMap<String, Object>(); // 6 gl
        data.put("busDistance", 1.295);
        input = new ExecData(GAME, ACTION, "prowler", data);
        execList.add(input);

        data = new HashMap<String, Object>(); // 57 gl
        data.put("busDistance", 18.534);
        input = new ExecData(GAME, ACTION, "prowler", data);
        execList.add(input);

        data = new HashMap<String, Object>(); // 1 gl
        data.put("busDistance", 16.915);
        input = new ExecData(GAME, ACTION, "prowler", data);
        execList.add(input);
        // tot 43.82 Km bus
    }

    @Override
    public void analyzeResult() {
        assertionPoint(GAME, 100.0, "prowler", "green leaves");

    }

}
