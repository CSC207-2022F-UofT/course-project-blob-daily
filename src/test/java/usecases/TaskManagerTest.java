package usecases;

import com.backend.QuestPetsApplication;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.TaskCompletionRecord;
import com.backend.usecases.AccountManager;
import com.backend.usecases.TaskManager;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

@SpringBootTest(classes = QuestPetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskManagerTest {
    private SessionID sessionID;
    private final String task = "attend lecture";
    private final String image = "https://www.saycampuslife.com/wp-content/uploads/2017/10/collegelectures-800x500_c.jpg";
    private final double reward = 100;

    @BeforeEach
    public void setup() {
        String username = "username";
        String password = "abc123!";
        sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(AccountManager.loginAccount(username, password).getBody())).get("sessionID"));
    }

    @AfterEach
    public void tearDown() {
        TaskManager.deleteAllCorrelatedCompletions(AccountManager.verifySession(sessionID));
        AccountManager.logoutAccount(sessionID);
    }

    @Test
    public void postCompletedTaskTest(){
        //action
        ResponseEntity<?> responseEntity = TaskManager.postCompletedTask(
                sessionID, task, image, reward
        );

        //assertion message
        String completeTaskMessage = "Unable finish task due to invalid session";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.OK, completeTaskMessage);
    }

    @Test
    public void postCompletedTaskInvalidSessionTest(){
        //action
        ResponseEntity<?> responseEntity = TaskManager.postCompletedTask(
                new SessionID("bad id"), task, image, reward
        );

        //assertion message
        String completeTaskMessage = "Unable finish task due to invalid session";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, completeTaskMessage);
    }

    @Test
    public void postCompletedTaskAlreadyCompleteTest(){
        //setup
        TaskManager.postCompletedTask(
                sessionID, task, image, reward
        );
        ResponseEntity<?> responseEntity = TaskManager.postCompletedTask(
                sessionID, task, image, reward
        );

        //assertion message
        String completeTaskMessage = "Unable finish task as it is already completed";

        //assertion statement
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, completeTaskMessage);
    }

    @Test
    public void deleteAllCorrelatedCompletionsTest(){
        //setup
        AccountID accountID = AccountManager.verifySession(sessionID);

        //actions
        ResponseEntity<?> responseEntity = TaskManager.deleteAllCorrelatedCompletions(accountID);

        //assertion message
        String deleteAllMessage = "invalid account provided for given accountID";

        //assertion statements
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.OK, deleteAllMessage);
    }

    @Test
    public void getActiveTasksTest(){
        //actions
        ResponseEntity<?> responseEntity = TaskManager.getActiveTasks(sessionID);

        //assertion message
        String activeTaskMessage = "an invalid session id was provided";

        //assert
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.OK, activeTaskMessage);
    }

    @Test
    public void getActiveTasksInvalidTest(){
        //actions
        ResponseEntity<?> responseEntity = TaskManager.getActiveTasks(new SessionID("bad id"));

        //assertion message
        String activeTaskMessage = "an invalid session id was provided";

        //assert
        Assertions.assertSame(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST, activeTaskMessage);
    }

    @Test
    public void getRecordTest() {
        //setup
        TaskManager.postCompletedTask(sessionID, task, image, reward);
        TaskManager.postCompletedTask(sessionID, "another task", image, reward);

        //actions
        List<TaskCompletionRecord> records = TaskManager.getRecord(Objects.requireNonNull(AccountManager.verifySession(sessionID)));

        //assertion message
        String getRecordMessage = "invalid account provided for given accountID";

        //assertion statements
        Assertions.assertEquals(2, records.size(), getRecordMessage);
    }
}
