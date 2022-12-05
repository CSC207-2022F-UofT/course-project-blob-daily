package usecases.managers;


import com.backend.QuestPetsApplication;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.Pet;
import com.backend.entities.TaskActive;
import com.backend.entities.TaskCompletionRecord;
import com.backend.usecases.facades.AccountSystemFacade;
import com.backend.usecases.managers.*;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@SpringBootTest(classes = QuestPetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HealthManagerTest {
    private SessionID sessionID;
    private AccountID accountID;
    private final PetManager petManager;
    private final HealthManager healthManager;
    private final TaskManager taskManager;
    private List<TaskCompletionRecord> completionRecords;
    private final AccountSystemFacade accountSystemFacade;
    private final AccountManager accountManager;

    @Autowired
    public HealthManagerTest(PetManager petManager, HealthManager healthManager, TaskManager taskManager, AccountSystemFacade accountSystemFacade, AccountManager accountManager){
        this.petManager = petManager;
        this.healthManager = healthManager;
        this.taskManager = taskManager;
        this.accountSystemFacade = accountSystemFacade;
        this.accountManager = accountManager;
    }

    @BeforeEach
    public void healthManagerTestSetup(){
        String username = "reeeethegreat";
        String password = "abc123!";
        ResponseEntity<Object> register = this.accountSystemFacade.registerAccount(username, password);
        if (!(register.getStatusCode() == HttpStatus.OK)){
            sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(username, password).getBody())).get("sessionID"));
        } else  {
            sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(register.getBody())).get("sessionID"));
        }

        accountID = this.accountManager.getAccountIDByUsername("reeeethegreat");
        Assertions.assertNotNull(accountID);
        petManager.initializePet(accountID.getID());

        List<TaskActive> active = taskManager.newActiveTasks();
        String task = active.get(0).getName();

        String image = "https://www.saycampuslife.com/wp-content/uploads/2017/10/collegelectures-800x500_c.jpg";
        taskManager.completeTask(accountID, task, image);
        completionRecords = taskManager.getTaskCompletionRecords(accountID);
    }

    @AfterEach
    public void tearDown() {
        if (sessionID != null) {
            this.accountSystemFacade.logoutAccount(this.sessionID);
        }
    }

    @Test
    public void updateHealthTest(){
        //Expected
        double expected = 95;

        //Action
        healthManager.updateHealth(accountID.getID(), 10);

        Pet pet = petManager.getPet(accountID.getID());

        double actual = pet.getHealth();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void noHealthDecayTest(){
        healthManager.healthDecay(accountID.getID(), completionRecords);

        Pet pet = petManager.getPet(accountID.getID());

        double expected = 85;

        double actual = pet.getHealth();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void HealthDecayTest(){
        List<TaskCompletionRecord> completed = new ArrayList<>();
        healthManager.healthDecay(accountID.getID(), completed);

        Pet pet = petManager.getPet(accountID.getID());

        double expected = 80;

        double actual = pet.getHealth();

        Assertions.assertEquals(expected, actual);
    }
}
