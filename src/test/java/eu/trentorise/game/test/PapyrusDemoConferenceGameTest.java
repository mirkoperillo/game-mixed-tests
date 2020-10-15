package eu.trentorise.game.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.PlayerService;

public class PapyrusDemoConferenceGameTest extends GameTest {

    @Autowired
    PlayerService playerSrv;

    private static final String GAME = "papyrus-demo-conference";
    private static final String DOMAIN = "my-domain";
    
    @Autowired
    MongoTemplate mongo;

    @Override
    public void initEnv() {
    	// John is already at level-1
        Map<String, Object> customData = new HashMap<>();
        customData.put("level", "level-1");
        savePlayerState(GAME, "john", new ArrayList<>(), customData);
    }
    

    @Override
    public void defineGame() {
    	 mongo.getDb().drop();
        List<GameConcept> concepts = new ArrayList<GameConcept>();
        concepts.add(new PointConcept("moves"));
        concepts.add(new PointConcept("errors"));
        concepts.add(new PointConcept("points"));
        concepts.add(new PointConcept("gold coins"));
        concepts.add(new PointConcept("timeSpent"));

        String action = "taskCompleted";

        defineGameHelper(DOMAIN, GAME, Arrays.asList(action, "playerInvited"), concepts);

        try {
			loadClasspathRules(GAME, "rules/"+GAME);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    @Override
    public void defineExecData(List<ExecData> execList) {
        Map<String, Object> d = new HashMap<String, Object>();
        d.put("errors", 0.0);
        d.put("timeSpent", 20);
        ExecData data = new ExecData(GAME, "taskCompleted", "alice", d);
        execList.add(data);
        
        d = new HashMap<String, Object>();
        d.put("errors", 0.0);
        d.put("timeSpent", 69);
        data = new ExecData(GAME, "taskCompleted", "jack", d);
        execList.add(data);
        
        d = new HashMap<String, Object>();
        d.put("errors", 0.0);
        d.put("timeSpent", 69);
        data = new ExecData(GAME, "taskCompleted", "john", d);
        execList.add(data);
        
        d = new HashMap<String, Object>();
        data = new ExecData(GAME, "playerInvited", "xavier", d);
        execList.add(data);
        
        d = new HashMap<String, Object>();
        data = new ExecData(GAME, "playerInvited", "xavier", d);
        execList.add(data);

    }

    @Override
    public void analyzeResult() {
    	assertionPoint(GAME, 3.0, "alice", "gold coins");
    	assertionPoint(GAME, 50.0 + 40.0, "alice", "points");
    	
    	assertionPoint(GAME, 2.0, "jack", "gold coins");
    	assertionPoint(GAME, 50.0 + 15.0, "jack", "points");
    	
    	assertionPoint(GAME, 1.0, "john", "gold coins");
    	assertionPoint(GAME, 70.0 + 35.0, "john", "points");
    	
    	assertionPoint(GAME, 2.0, "xavier", "gold coins");
    	assertionPoint(GAME, 200.0, "xavier", "points");
    	
    }

}
