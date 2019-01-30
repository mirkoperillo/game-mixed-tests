package eu.trentorise.game.test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import eu.trentorise.game.model.ChallengeModel;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.PointConceptStateHelperFactory;
import eu.trentorise.game.model.PointConceptStateHelperFactory.PointConceptStateHelper;
import eu.trentorise.game.model.core.ChallengeAssignment;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;

public class RepetitiveBehaviourChallengeInTimeTest extends GameTest {

    private static final String GAME = "repetitiveBehaviourChallenge";
    private static final String ACTION = "save_itinerary";
    private static final String PLAYER = "Hermione Granger";

    @Autowired
    private GameService gameSrv;

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private PointConceptStateHelperFactory helperFactory;

    private double score = 0;

    @Autowired
    private MongoTemplate mongo;

    @Before
    public final void cleanDBChalleng() {
        // clean mongo
        mongo.getDb().dropDatabase();
    }

    @Override
    public void initEnv() {
        PointConceptStateHelper h = helperFactory.instanceOf(GAME, "Train_Trips");
        h.setScoreInTime(parseFromString("2018-01-26"), 2d);
        h.setScoreInTime(parseFromString("2018-01-29"), 4d);
        h.setScoreInTime(parseFromString("2018-01-30"), 6d);

        savePlayerState(GAME, PLAYER, Arrays.asList(h.build()));

        // assign a challenge to PLAYER
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("target", 2d);
        data.put("counterName", "Train_Trips");
        data.put("periodName", "daily");
        data.put("periodTarget", 3d);
        data.put("bonusScore", 400d);
        data.put("bonusPointType", "green leaves");
        ChallengeAssignment challenge = new ChallengeAssignment("repetitiveBehaviour",
                "repetitiveBehaviourInstance", data, "assigned",
                parseFromString("2018-01-27"),
                parseFromString("2018-02-03"));
        playerSrv.assignChallenge(GAME, PLAYER, challenge);


    }

    private Date parseFromString(String dateString) {
        return Date
                .from(LocalDate.parse(dateString).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }


    private PointConcept create(String name) {
        PointConcept p = new PointConcept(name);

        Date startDate = parseFromString("2017-09-09");
        long dayDurationInMillis = 24 * 60 * 60 * 1000;
        p.addPeriod("daily", startDate, dayDurationInMillis);
        p.addPeriod("weekly", startDate, dayDurationInMillis * 7);

        return p;
    }

    @Override
    public void defineGame() {
        List<GameConcept> concepts = new ArrayList<GameConcept>();

        concepts.add(create("green leaves"));
        concepts.add(create("Train_Trips"));
        concepts.add(create("Train_Km"));


        defineGameHelper(null, GAME, Arrays.asList(ACTION), concepts);

        try {
            loadClasspathRules(GAME, "rules/repetitiveBehaviourChallenge");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // loadClasspathRules(GAME, Arrays.asList("rules/" + GAME + "/repetitiveBehaviour.drl"));

        // define challenge models
        ChallengeModel model = new ChallengeModel();
        model.setName("repetitiveBehaviour");
        model.setVariables(new HashSet<String>());
        model.getVariables().add("target");
        model.getVariables().add("counterName");
        model.getVariables().add("periodTarget");
        model.getVariables().add("periodName");
        model.getVariables().add("bonusScore");
        model.getVariables().add("bonusPointType");
        gameSrv.saveChallengeModel(GAME, model);

    }

    @Override
    public void defineExecData(List<ExecData> execList) {
        Map<String, Object> data = new HashMap<>();
        data.put("trainDistance", 20d);
        execList.add(new ExecData(GAME, ACTION, PLAYER, data));

        data = new HashMap<>();
        data.put("trainDistance", 40d);
        execList.add(new ExecData(GAME, ACTION, PLAYER, data));
    }


    @Override
    public void analyzeResult() {
        // assertionPoint(GAME, score, PLAYER, "green leaves");
    }

}
