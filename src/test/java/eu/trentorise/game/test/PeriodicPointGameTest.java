package eu.trentorise.game.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.PlayerService;

public class PeriodicPointGameTest extends GameTest {

    @Autowired
    PlayerService playerSrv;

    private static final String GAME = "periodicScore";
    private static final String ACTION = "save_itinerary";
    private static final String DOMAIN = "my-domain";

    @Override
    public void initEnv() {

    }

    @Override
    public void defineGame() {
        List<GameConcept> concepts = new ArrayList<GameConcept>();
        PointConcept p = new PointConcept("green leaves");

        Date startDate = new Date(); // today
        long dayDurationInMillis = 24 * 60 * 60 * 1000;
        p.addPeriod("weekly", startDate, dayDurationInMillis);
        concepts.add(p);

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
        data.put("walkDistance", 1d);
        ExecData input = new ExecData(GAME, ACTION, "prowler", data);
        execList.add(input);

        DateTimeUtils
                .setCurrentMillisFixed(new GregorianCalendar(2016, 3, 26, 3, 22).getTimeInMillis());
        data = new HashMap<String, Object>();
        data.put("bikeDistance", 5d);
        input = new ExecData(GAME, ACTION, "prowler", data);
        execList.add(input);

    }

    @Override
    public void analyzeResult() {
        assertionPoint(GAME, 30d, "prowler", "green leaves");
    }

}
