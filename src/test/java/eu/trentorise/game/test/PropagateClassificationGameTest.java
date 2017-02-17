package eu.trentorise.game.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.task.GeneralClassificationTask;

public class PropagateClassificationGameTest extends GameTest {

	@Autowired
	PlayerService playerSrv;

	private static final String GAME = "propagateClassification";
	private static final String ACTION = "increase_score";

	@Override
	public void initEnv() {
		TeamState team = new TeamState(GAME, "school");
		team.setName("school");
		team.setMembers(Arrays.asList("classroom", "volunteer"));
		playerSrv.saveTeam(team);

		team = new TeamState(GAME, "classroom");
		team.setName("classroom");
		team.setMembers(Arrays.asList("alice"));
		playerSrv.saveTeam(team);

	}

	@Override
	public void defineGame() {
		List<GameConcept> concepts = new ArrayList<GameConcept>();
		concepts.add(new PointConcept("steps"));
		concepts.add(new BadgeCollectionConcept("travel"));

		defineGameHelper(GAME, Arrays.asList(ACTION), concepts);

		loadClasspathRules(GAME, Arrays.asList("rules/" + GAME + "/constants",
				"rules/" + GAME + "/ruleBadges.drl", "rules/" + GAME
						+ "/rulePoint.drl", "rules/" + GAME
						+ "/weeklytasks.drl"));

		TaskSchedule schedule = new TaskSchedule();
		schedule.setCronExpression("");
		GeneralClassificationTask task = new GeneralClassificationTask(
				schedule, "steps", "team classification weekly");
		addGameTask(GAME, task);

	}

	@Override
	public void defineExecData(List<ExecData> execList) {
	}

	@Override
	public void analyzeResult() {
	}

}
