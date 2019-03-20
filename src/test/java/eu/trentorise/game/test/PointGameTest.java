package eu.trentorise.game.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.PlayerService;

public class PointGameTest extends GameTest {

    @Autowired
    PlayerService playerSrv;

    private static final String GAME = "test"; // no rules for this game in resources..no useful
                                               // test
    private static final String ACTION = "save_itinerary";
    private static final String DOMAIN = "my-domain";

    @Override
    public void initEnv() {

    }

    @Override
    public void defineGame() {
        List<GameConcept> concepts = new ArrayList<GameConcept>();
        concepts.add(new PointConcept("green leaves"));
        concepts.add(new PointConcept("green leaves week 2"));
        concepts.add(new PointConcept("green leaves week 3"));
        concepts.add(new PointConcept("green leaves week 4"));
        concepts.add(new PointConcept("green leaves week 5"));
        concepts.add(new PointConcept("green leaves week 6"));
        concepts.add(new PointConcept("green leaves week 7"));
        concepts.add(new PointConcept("green leaves week 8"));
        concepts.add(new PointConcept("green leaves week 9"));

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

    }

}
