package eu.trentorise.game.test;

import java.io.IOException;
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

public class MultilevelTeamsGameTest extends GameTest {

    @Autowired
    PlayerService playerSrv;

    private static final String GAME = "multilevelteams";
    private static final String ACTION = "increase_score";
    private static final String DOMAIN = "my-domain";


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

        team = new TeamState(GAME, "classroom-B");
        team.setName("classroom-B");
        team.setMembers(Arrays.asList("bob", "teacherSmith"));
        playerSrv.saveTeam(team);

        team = new TeamState(GAME, "classroom-C");
        team.setName("classroom-C");
        team.setMembers(Arrays.asList("cindy", "frank", "teacherSmith"));
        playerSrv.saveTeam(team);
    }

    @Override
    public void defineGame() {
        List<GameConcept> concepts = new ArrayList<GameConcept>();
        concepts.add(new PointConcept("steps"));
        concepts.add(new BadgeCollectionConcept("travel"));

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
        data.put("meters-walked", 500d);
        ExecData input = new ExecData(GAME, ACTION, "alice", data);
        execList.add(input);

        data = new HashMap<String, Object>();
        data.put("meters-walked", 100d);
        input = new ExecData(GAME, ACTION, "volunteer", data);
        execList.add(input);

        data = new HashMap<String, Object>();
        data.put("meters-walked", 10d);
        input = new ExecData(GAME, ACTION, "classroom", data);
        execList.add(input);

        data = new HashMap<String, Object>();
        data.put("meters-walked", 30d);
        input = new ExecData(GAME, ACTION, "teacherSmith", data);
        execList.add(input);
    }

    @Override
    public void analyzeResult() {
        assertionPoint(GAME, 500d, "alice", "steps");
        assertionPoint(GAME, 100d, "volunteer", "steps");
        assertionPoint(GAME, 510d, "classroom", "steps");
        assertionPoint(GAME, 30d, "classroom-B", "steps");
        assertionPoint(GAME, 30d, "classroom-C", "steps");
        assertionPoint(GAME, 610d, "school", "steps");
        assertionPoint(GAME, 30d, "teacherSmith", "steps");
    }

}
