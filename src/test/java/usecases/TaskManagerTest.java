package usecases;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.Objects;

public class TaskManagerTest {
    private SessionID sessionID;
    private final String username = "username";
    private final String password = "abc123!";

    @BeforeEach
    public void setup() {
        ResponseEntity<Object> register = AccountManager.registerAccount(this.username, this.password);

        if (!(register.getStatusCode() == HttpStatus.OK)){
            sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(AccountManager.loginAccount(this.username, this.password).getBody())).get("sessionID"));
        } else  {
            sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(register.getBody())).get("sessionID"));
        }

    }

    @AfterEach
    public void tearDown() {
        if (sessionID != null) {
            AccountManager.logoutAccount(this.sessionID);
        }
    }
    @Test
    public void postCompletedTaskTest(){
        //values
        AccountID accountID = AccountManager.verifySession(sessionID);
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
        String task = "Attend lecture";
        String image = "https://cdn.eftm.com/wp-content/uploads/2019/10/Screen-Shot-2019-10-15-at-2.13.23-pm.png";
        double reward = 5;

        //actions
        assert accountID != null;
        ResponseEntity<?> expectedOutcome = new ResponseEntity<>(new TaskCompletionRecord(accountID, timestamp, task, image), HttpStatus.OK);

        //assertion message
        String taskRecordMessage = "Could not create a Task Completion Record with the given parameters";

        //assertion statement
        Assertions.assertEquals(expectedOutcome, TaskManager.postCompletedTask(sessionID, task, image, reward));
    }

    @Test
    public void postCompletedTaskInvalidTest(){

    }

    @Test
    public void deleteAllCorrelatedCompletionsTest(){

    }

    @Test
    public void deleteAllCorrelatedCompletionsInvalidTest(){

    }

    @Test
    public void getActiveTasksTest(){

    }

    @Test
    public void getActiveTasksInvalidTest(){

    }
}
