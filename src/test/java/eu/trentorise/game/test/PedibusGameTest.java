package eu.trentorise.game.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.PlayerService;

public class PedibusGameTest extends GameTest {

	@Autowired
	PlayerService playerSrv;

	private static final String GAME = "teams";
	private static final String ACTION = "increase_score";

	@Override
	public void initEnv() {
		TeamState team = new TeamState(GAME, "fuorileggeId");
		team.setName("fuorilegge");
		team.setMembers(Arrays.asList("prowler"));
		playerSrv.saveTeam(team);

		team = new TeamState(GAME, "sinister six");
		team.setName("sinister six");
		team.setMembers(Arrays.asList("sandman"));
		playerSrv.saveTeam(team);
	}

	@Override
	public void defineGame() {
		List<GameConcept> concepts = new ArrayList<GameConcept>();
		concepts.add(new PointConcept("steps"));
		concepts.add(new BadgeCollectionConcept("Meano-FliessItinerary"));

		defineGameHelper(GAME, Arrays.asList(ACTION), concepts);

		String pathGame = "/home/mirko/data/git/smartcampus.gamification/game-engine.games/pedibus-1exp";
		loadFilesystemRules(
				GAME,
				Arrays.asList(pathGame + "/constants", pathGame
						+ "/ruleBadges.drl", pathGame + "/rulePoint.drl"));

	}

	@Override
	public void defineExecData(List<ExecData> execList) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("meters-walked", 500d);
		ExecData input = new ExecData(GAME, ACTION, "prowler", data);
		execList.add(input);

		data = new HashMap<String, Object>();
		data.put("meters-walked", 100d);
		input = new ExecData(GAME, ACTION, "fuorileggeId", data);
		execList.add(input);

		data = new HashMap<String, Object>();
		data.put("meters-walked", 11600d);
		input = new ExecData(GAME, ACTION, "sandman", data);
		execList.add(input);
	}

	@Override
	public void analyzeResult() {
		assertionPoint(GAME, 750d, "prowler", "steps");
		assertionPoint(GAME, 900d, "fuorileggeId", "steps");
		assertionPoint(GAME, 17400d, "sandman", "steps");
		assertionPoint(GAME, 17400d, "sinister six", "steps");
		assertionBadge(GAME, Arrays.asList("Fai della Paganella"),
				"sinister six", "Meano-FliessItinerary");
	}

}
