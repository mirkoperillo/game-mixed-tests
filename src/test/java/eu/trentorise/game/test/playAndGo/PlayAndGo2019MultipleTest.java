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

public class PlayAndGo2019MultipleTest extends GameTest {

	@Autowired
	private PlayerService playerSrv;

	@Autowired
	private GameService gameSrv;

	@Autowired
	private MongoTemplate mongo;

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
		assignment.getData().put("surveyType", "start");
		playerSrv.assignChallenge(GAME, "gambit", assignment);
		
		assignment = new ChallengeAssignment();
		assignment.setModelName("survey");
		assignment.setData(new HashMap<>());
		assignment.getData().put("bonusScore", 122.0);
		assignment.getData().put("bonusPointType", "green leaves");
		assignment.getData().put("surveyType", "end");
		playerSrv.assignChallenge(GAME, "wolverine", assignment);
		
		assignment.setModelName("absoluteIncrement");
        assignment.setData(new HashMap<>());
        assignment.getData().put("periodName", "weekly");
        assignment.getData().put("bonusScore", 40.0);
        assignment.getData().put("bonusPointType", "green leaves");
        assignment.getData().put("target", 110.0);
        assignment.getData().put("counterName", "green leaves");
        playerSrv.assignChallenge(GAME, "wolverine", assignment);
	}

	@Override
	public void defineGame() {
		mongo.getDb().drop();

		List<String> scoreNames = Arrays.asList("green leaves", "PandR_Trips", "Transit_Trips", "BikeSharing_Km",
				"Bike_Trips", "Walk_Trips", "Walk_Km", "Car_Trips", "NoCar_Trips", "Bus_Km", "BikeSharing_Trips",
				"Car_Km", "Train_Km", "Recommendations", "Bus_Trips", "Train_Trips", "Bike_Km", "ZeroImpact_Trips");

		final Date startDate = LocalDate.now().toDate();
		final long dayDurationInMillis = 24 * 60 * 60 * 1000;
		List<GameConcept> concepts = scoreNames.stream().map(s -> {
			PointConcept p = new PointConcept(s);
			p.addPeriod("weekly", startDate, dayDurationInMillis);
			p.addPeriod("daily", startDate, dayDurationInMillis);
			return p;
		}).collect(Collectors.toList());

		concepts.add(new BadgeCollectionConcept("green leaves"));

		defineGameHelper(DOMAIN, GAME, Arrays.asList(ACTION, "survey_complete", "end_survey_complete"), concepts);

		try {
			loadClasspathRules(GAME, "rules/" + GAME);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// absolute increment model
		ChallengeModel absoluteIncrement = new ChallengeModel();
		absoluteIncrement.setGameId(GAME);
		absoluteIncrement.setName("absoluteIncrement");
		absoluteIncrement.getVariables().add("bonusScore");
		absoluteIncrement.getVariables().add("bonusPointType");
		absoluteIncrement.getVariables().add("difficulty");
		absoluteIncrement.getVariables().add("wi");
		absoluteIncrement.getVariables().add("periodName");
		absoluteIncrement.getVariables().add("target");
		absoluteIncrement.getVariables().add("counterName");
		gameSrv.saveChallengeModel(GAME, absoluteIncrement);

		// new badge challenge
		ChallengeModel nextBadge = new ChallengeModel();
		nextBadge.setGameId(GAME);
		nextBadge.setName("nextBadge");
		nextBadge.getVariables().add("bonusScore");
		nextBadge.getVariables().add("bonusPointType");
		nextBadge.getVariables().add("badgeCollectionName");
		nextBadge.getVariables().add("initialBadgeNum");
		nextBadge.getVariables().add("target");
		gameSrv.saveChallengeModel(GAME, nextBadge);

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
		ExecData input = null;
		data.put("walkDistance", 4.0);
		data.put("bikeDistance", 30.0);
		input = new ExecData(GAME, ACTION, "prowler", data);
		execList.add(input);
		
		data = new HashMap<String, Object>();
		data.put("surveyType", "start");
		input = new ExecData(GAME, "survey_complete", "gambit", data);
		execList.add(input);
		
		data = new HashMap<String, Object>();
		input = new ExecData(GAME, "end_survey_complete", "wolverine", data);
		execList.add(input);

	}

	@Override
	public void analyzeResult() {
		assertionPoint(GAME, 160.0, "prowler", "green leaves");
		assertionPoint(GAME, 4.0, "prowler", "Walk_Km");
		assertionPoint(GAME, 30.0, "prowler", "Bike_Km");
		assertionBadge(GAME, Arrays.asList("50_point_green", "100_point_green"), "prowler", "green leaves");
		
		assertionPoint(GAME, 100.0, "gambit", "green leaves");
		assertionBadge(GAME, Arrays.asList("50_point_green", "100_point_green"), "gambit", "green leaves");
		
		assertionPoint(GAME, 162.0, "wolverine", "green leaves");
		assertionBadge(GAME, Arrays.asList("50_point_green", "100_point_green"), "wolverine", "green leaves");
	}

}
