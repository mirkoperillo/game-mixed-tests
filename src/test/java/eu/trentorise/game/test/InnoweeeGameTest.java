package eu.trentorise.game.test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.services.PlayerService;

public class InnoweeeGameTest extends GameTest {

    private static final String GAME_ID = "innoweee";
    private static final String DOMAIN = "";

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private MongoTemplate mongo;

    @Override
    public void initEnv() {
        TeamState team = new TeamState(GAME_ID, "scuola");
        team.setName("scuola");
        team.setMembers(Arrays.asList("classe"));
        playerSrv.saveTeam(team);

    }


    private PointConcept attachPeriods(PointConcept pc) {
        pc.addPeriod("R1", date("01/04/2019"), date("02/04/2019"), -1);
        pc.addPeriod("R2", date("02/04/2019"), date("03/04/2019"), -1);
        pc.addPeriod("R3", date("03/04/2019"), date("04/04/2019"), -1);
        pc.addPeriod("R4", date("04/04/2019"), date("05/04/2019"), -1);
        pc.addPeriod("R5", date("05/04/2019"), date("06/04/2019"), -1);
        pc.addPeriod("R6", date("06/04/2019"), date("07/04/2019"), -1);
        // pc.addPeriod("R1", new Date(1548975600000L), new Date(1550358000000L), -1);
        // pc.addPeriod("R2", new Date(1550444400000L), new Date(1557007200000L), -1);
        // pc.addPeriod("R3", new Date(1557093600000L), new Date(1557612000000L), -1);
        // pc.addPeriod("R4", new Date(1557698400000L), new Date(1558216800000L), -1);
        // pc.addPeriod("R5", new Date(1558303200000L), new Date(1558821600000L), -1);
        // pc.addPeriod("R6", new Date(1558908000000L), new Date(1559426400000L), -1);

        return pc;
    }

    private Date date(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return Date.from(LocalDate.parse(dateString, formatter)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public void defineGame() {

        mongo.getDb().dropDatabase();

        List<String> actions = Arrays.asList("itemDelivery", "reduceReport", "buildRobot");

        List<GameConcept> concepts = new ArrayList<>();

        List<String> scoreNames =
                Arrays.asList("reduceCoin", "reuseCoin", "recycleCoin", "totalItems", "totalReuse",
                        "totalRecycle", "totalReduce", "weight", "CO2", "plastic", "glass", "iron",
                        "aluminium", "copper", "tin", "nickel", "silver", "gold", "platinum");

        for (String name : scoreNames) {
            PointConcept pc = new PointConcept(name);
            pc = attachPeriods(pc);
            concepts.add(pc);
        }

        defineGameHelper(DOMAIN, GAME_ID, actions, concepts);

        try {
            loadClasspathRules(GAME_ID, "rules/" + GAME_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void defineExecData(List<ExecData> execList) {
        Map<String, Object> data = null;

        // Date dateR2 = date("22/01/2019");
        // data = new HashMap<String, Object>();
        // data.put("reduceCoin", 1d);
        // data.put("reuseCoin", 0d);
        // data.put("recycleCoin", 3d);
        // ExecData build = new ExecData(GAME_ID, "buildRobot", "classe", data, dateR2);
        // execList.add(build);

        // Date dateR1 = date("02/03/2019");
        // data = new HashMap<String, Object>();
        // data.put("reduceCoin", 5d);
        // ExecData reduceReport = new ExecData(GAME_ID, "reduceReport", "classe", data, dateR1);
        // execList.add(reduceReport);


        Date dateR3 = date("25/01/2019");
        data = new HashMap<String, Object>();
        data.put("weee", false);
        data.put("weight", 5d);
        data.put("plastic", 2d);
        data.put("glass", 1d);
        data.put("iron", 1d);
        data.put("aluminium", 3d);
        data.put("copper", 5d);
        data.put("tin", 6d);
        data.put("nickel", 7d);
        data.put("silver", 8d);
        data.put("gold", 8d);
        data.put("platinum", 3d);
        ExecData itemDelivery = new ExecData(GAME_ID, "itemDelivery", "classe", data, dateR3);
        execList.add(itemDelivery);

    }

    @Override
    public void analyzeResult() {

    }

}
