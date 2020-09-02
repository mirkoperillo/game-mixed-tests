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

public class PlayAndGo2020FerraraGameTest extends GameTest {

    @Autowired
    PlayerService playerSrv;

    private static final String GAME = "playAndGo2020-ferrara";
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
                "Train_Trips", "Bike_Km", "ZeroImpact_Trips", "Carpooling_Km", "Carpooling_Trips");


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


    /**
     * walk 0.868914253399 -> 13.0
     * 
     * walk 0.852623028596 -> 12.0 (13)
     * 
     * bike 15.281386806 -> 69.0
     * 
     * bike 5.16915840793 -> 25.0 (26)
     * 
     * bus 0.808582531226 -> 1.0 (2.0)
     * 
     * bus 28.3243579276 -> 53.0
     * 
     * train 83.5583255584 -> 64.0 (65)
     * 
     * train 23.6698031683 -> 23.0 (24)
     * 
     */
    @Override
    public void defineExecData(List<ExecData> execList) {
        // Map<String, Object> data = new HashMap<String, Object>();
        // ExecData input = null;
        // data.put("walkDistance", 10.0);
        // input = new ExecData(GAME, ACTION, "prowler", data);
        // execList.add(input);

        // data = new HashMap<String, Object>();
        // data.put("walkDistance", 0.0);
        // input = new ExecData(GAME, ACTION, "prowler", data);
        // execList.add(input);

        // data = new HashMap<String, Object>();
        // data.put("trainDistance", 3.5);
        // input = new ExecData(GAME, ACTION, "prowler", data);
        // execList.add(input);

        // data = new HashMap<String, Object>();
        // data.put("trainDistance", 10.5);
        // input = new ExecData(GAME, ACTION, "prowler", data);
        // execList.add(input);

        // test carpooling
        // Wolverine driver, JeanGrey, Cyclops passengers
        Map<String, Object> data = new HashMap<String, Object>();
        ExecData input = null;

        // Wolverine - JeanGrey bonus
        data.put("carpoolingDistance", 10.0);
        data.put("driverTrip", true);
        data.put("firstPair", true);
        input = new ExecData(GAME, ACTION, "Wolverine", data);
        execList.add(input);

        // JeanGrey passenger
        data = new HashMap<String, Object>();
        data.put("carpoolingDistance", 10.0);
        data.put("driverTrip", false);
        input = new ExecData(GAME, ACTION, "JeanGrey", data);
        execList.add(input);

        // Cyclops passenger
        data = new HashMap<String, Object>();
        data.put("carpoolingDistance", 10.0);
        data.put("driverTrip", false);
        input = new ExecData(GAME, ACTION, "Cyclops", data);
        execList.add(input);

        // Wolverine - Cyclops bonus
        data = new HashMap<String, Object>();
        data.put("carpoolingDistance", 10.0);
        data.put("driverTrip", true);
        data.put("firstPair", false);
        input = new ExecData(GAME, ACTION, "Wolverine", data);
        execList.add(input);

        // Wolverine - walk
        data = new HashMap<String, Object>();
        data.put("walkDistance", 1.0);
        input = new ExecData(GAME, ACTION, "Wolverine", data);
        execList.add(input);

    }

    @Override
    public void analyzeResult() {
        assertionPoint(GAME, 10.0, "Wolverine", "Carpooling_Km");
        assertionPoint(GAME, 1.0, "Wolverine", "Carpooling_Trips");
        assertionPoint(GAME, 10.0, "JeanGrey", "Carpooling_Km");
        assertionPoint(GAME, 1.0, "JeanGrey", "Carpooling_Trips");
        assertionPoint(GAME, 10.0, "Cyclops", "Carpooling_Km");
        assertionPoint(GAME, 1.0, "Cyclops", "Carpooling_Trips");

        // 7.0 carpooling + 15.0 1Km Walk
        assertionPoint(GAME, 22.0, "Wolverine", "green leaves");

        assertionPoint(GAME, 5.0, "JeanGrey", "green leaves");
        assertionPoint(GAME, 5.0, "Cyclops", "green leaves");

        assertionPoint(GAME, 1.0, "Wolverine", "NoCar_Trips");
    }

}
