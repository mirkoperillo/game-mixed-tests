package eu.trentorise.game.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.PlayerService;

public class PapyrusGameTest extends GameTest {

    @Autowired
    PlayerService playerSrv;

    private static final String GAME = "papyrus";
    private static final String DOMAIN = "my-domain";

    @Override
    public void initEnv() {
        Map<String, Object> customData = new HashMap<>();
        customData.put("level", "level-1");
        savePlayerState("papyrus", "alice", new ArrayList<>(), customData);
    }

    @Override
    public void defineGame() {
        List<GameConcept> concepts = new ArrayList<GameConcept>();
        concepts.add(new PointConcept("moves"));
        concepts.add(new PointConcept("errors"));
        concepts.add(new PointConcept("points"));
        concepts.add(new PointConcept("gold coins"));

        String action = "taskCompleted";

        defineGameHelper(DOMAIN, GAME, Arrays.asList(action), concepts);

        loadClasspathRules(GAME,
                Arrays.asList("rules/papyrus/levels.drl", "rules/papyrus/rewards.drl"));

    }

    @Override
    public void defineExecData(List<ExecData> execList) {
        Map<String, Object> d = new HashMap<String, Object>();
        d.put("moves", 2.0);
        d.put("errors", 0.0);

        ExecData data = new ExecData(GAME, "taskCompleted", "alice", d);
        execList.add(data);

    }

    @Override
    public void analyzeResult() {}

}
