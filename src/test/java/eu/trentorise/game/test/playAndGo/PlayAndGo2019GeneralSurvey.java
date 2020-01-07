package eu.trentorise.game.test.playAndGo;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.ChallengeModel;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.ChallengeAssignment;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.test.GameTest;

public class PlayAndGo2019GeneralSurvey extends GameTest {

    @Autowired
    PlayerService playerSrv;

    @Autowired
    private GameService gameSrv;

    @Autowired
    MongoTemplate mongo;

    private static final String GAME = "playAndGo2019";
    private static final String ACTION = "save_itinerary";
    private static final String DOMAIN = "";


    @Override
    public void initEnv() {
        ChallengeAssignment assignment = new ChallengeAssignment();
        assignment.setModelName("survey");
        assignment.setData(new HashMap<>());
        assignment.getData().put("bonusScore", 100.0);
        assignment.getData().put("bonusPointType", "green leaves");
        assignment.getData().put("surveyType", "newFormat");
        playerSrv.assignChallenge(GAME, "prowler", assignment);

        assignment = new ChallengeAssignment();
        assignment.setModelName("survey");
        assignment.setData(new HashMap<>());
        assignment.getData().put("bonusScore", 20.0);
        assignment.getData().put("bonusPointType", "green leaves");
        assignment.getData().put("surveyType", "end");
        playerSrv.assignChallenge(GAME, "prowler", assignment);
    }

    @Override
    public void defineGame() {
        mongo.getDb().drop();

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



        defineGameHelper(DOMAIN, GAME,
                Arrays.asList(ACTION, "end_survey_complete", "survey_complete"), concepts);

        try {
            loadClasspathRules(GAME, "rules/" + GAME);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // survey challenge model
        ChallengeModel survey = new ChallengeModel();
        survey.setGameId(GAME);
        survey.setName("survey");
        survey.getVariables().add("bonusScore");
        survey.getVariables().add("bonusPointType");
        survey.getVariables().add("surveyType");
        survey.getVariables().add("link");
        gameSrv.saveChallengeModel(GAME, survey);
    }


    @Override
    public void defineExecData(List<ExecData> execList) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("surveyType", "newFormat");
        ExecData input = null;
        input = new ExecData(GAME, "survey_complete", "prowler", data);
        execList.add(input);

        input = new ExecData(GAME, "end_survey_complete", "prowler", data);
        execList.add(input);
    }

    @Override
    public void analyzeResult() {
        assertionPoint(GAME, 120.0, "prowler", "green leaves");
    }

}
