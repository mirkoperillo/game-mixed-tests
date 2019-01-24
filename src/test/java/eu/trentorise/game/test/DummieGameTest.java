package eu.trentorise.game.test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.PointConceptStateHelperFactory;
import eu.trentorise.game.model.PointConceptStateHelperFactory.PointConceptStateHelper;
import eu.trentorise.game.model.core.GameConcept;

public class DummieGameTest extends GameTest {

    @Autowired
    private PointConceptStateHelperFactory helperFactory;

    private static final String GAME = "periodicScore";
    private static final String ACTION = "save_itinerary";
    private static final String DOMAIN = "my-domain";


    @Override
    public void initEnv() {
        Date moment = Date.from(
                LocalDate.now().minusDays(5).atStartOfDay(ZoneId.systemDefault()).toInstant());

        PointConceptStateHelper h =
                helperFactory.instanceOf(GAME, "green leaves").setScoreInTime(moment, 5d);
        moment = Date.from(
                LocalDate.now().minusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant());
        h.setScoreInTime(moment, 12d);
        moment = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        h.setScoreInTime(moment, 13d);
        savePlayerState(GAME, "gambit", Arrays.asList(h.build()));


    }

    @Override
    public void defineGame() {
        List<GameConcept> concepts = new ArrayList<GameConcept>();
        PointConcept p = new PointConcept("green leaves");

        Date startDate = Date.from(
                LocalDate.now().minusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant());
        long dayDurationInMillis = 24 * 60 * 60 * 1000;
        p.addPeriod("weekly", startDate, dayDurationInMillis);
        concepts.add(p);

        defineGameHelper(DOMAIN, GAME, Arrays.asList(ACTION), concepts);

    }

    @Override
    public void defineExecData(List<ExecData> execList) {
        // TODO Auto-generated method stub

    }

    @Override
    public void analyzeResult() {
        // TODO Auto-generated method stub

    }

}
