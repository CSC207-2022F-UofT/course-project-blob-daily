package usecases.facades;

import com.backend.QuestPetsApplication;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.TaskActive;
import com.backend.repositories.TaskActiveRepo;
import com.backend.usecases.facades.AccountSystemFacade;
import com.backend.usecases.facades.TaskSystemFacade;
import com.backend.usecases.managers.AccountManager;
import com.backend.usecases.managers.TaskManager;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@SpringBootTest(classes = QuestPetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskSystemFacadeTest {
    private SessionID sessionID;
    private String task;
    private final String image = "https://www.saycampuslife.com/wp-content/uploads/2017/10/collegelectures-800x500_c.jpg";
    private double reward;

    private final TaskManager taskManager;
    private final TaskActiveRepo taskActiveRepo;
    private final TaskSystemFacade taskSystemFacade;
    private final AccountSystemFacade accountSystemFacade;
    private final AccountManager accountManager;

    @Autowired
    public TaskSystemFacadeTest(TaskManager taskManager, TaskActiveRepo taskActiveRepo, TaskSystemFacade taskSystemFacade, AccountSystemFacade accountSystemFacade, AccountManager accountManager) {
        this.taskManager = taskManager;
        this.taskActiveRepo = taskActiveRepo;
        this.taskSystemFacade = taskSystemFacade;
        this.accountSystemFacade = accountSystemFacade;
        this.accountManager = accountManager;
    }

    @BeforeEach
    public void setup() {
        String username = "gochugum";
        String password = "abc123!";
        TaskActive active = taskActiveRepo.findAll().get(0);

        sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(username, password).getBody())).get("sessionID"));
        task = active.getName();
        reward = active.getReward();
    }

    @AfterEach
    public void tearDown() {
        taskManager.deleteAllCorrelatedCompletions(this.accountManager.verifySession(sessionID));
        this.accountSystemFacade.logoutAccount(sessionID);
    }

    @Test
    public void postCompletedTaskTest(){
        //action
        ResponseEntity<?> responseEntity = this.taskSystemFacade.completeTask(sessionID, task, image, reward);

        //assertion message
        String completeTaskMessage = "Unable finish task due to invalid session";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.OK, completeTaskMessage);
    }

    @Test
    public void postCompletedTaskInvalidSessionTest(){
        //action
        ResponseEntity<?> responseEntity = taskSystemFacade.completeTask(new SessionID("bad id"), task, image, reward);

        //assertion message
        String completeTaskMessage = "Unable finish task due to invalid session";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, completeTaskMessage);
    }

    @Test
    public void postCompletedTaskInvalidTaskTest(){
        //action
        ResponseEntity<?> responseEntity = taskSystemFacade.completeTask(sessionID, "not a task", image, reward);

        //assertion message
        String completeTaskMessage = "Unable finish task as it does not exist";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, completeTaskMessage);
    }

    @Test
    public void postCompletedTaskInvalidRewardTest(){
        //action
        ResponseEntity<?> responseEntity = taskSystemFacade.completeTask(sessionID, task, image, 10000);

        //assertion message
        String completeTaskMessage = "Unable finish task as reward does not match";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, completeTaskMessage);
    }

    @Test
    public void postCompletedTaskAlreadyCompleteTest(){
        //setup
        taskSystemFacade.completeTask(sessionID, task, image, reward);
        ResponseEntity<?> responseEntity = taskSystemFacade.completeTask(sessionID, task, image, reward);

        //assertion message
        String completeTaskMessage = "Unable finish task as it is already completed";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, completeTaskMessage);
    }

    @Test
    public void getActiveTasksTest(){
        //actions
        ResponseEntity<?> responseEntity = taskSystemFacade.getActiveTasks(sessionID);

        //assertion message
        String activeTaskMessage = "an invalid session id was provided";

        //assert
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.OK, activeTaskMessage);
    }

    @Test
    public void getActiveTasksInvalidTest(){
        //actions
        ResponseEntity<?> responseEntity = taskSystemFacade.getActiveTasks(new SessionID("bad id"));

        //assertion message
        String activeTaskMessage = "an invalid session id was provided";

        //assert
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, activeTaskMessage);
    }
}