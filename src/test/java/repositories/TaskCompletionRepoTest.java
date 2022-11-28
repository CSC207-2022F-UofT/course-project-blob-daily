package repositories;

import com.backend.QuestPetsApplication;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.TaskCompletionRecord;
import com.backend.repositories.TaskCompletionRepo;
import com.backend.usecases.AccountManager;
import com.backend.usecases.TaskManager;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Objects;

@SpringBootTest(classes = QuestPetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskCompletionRepoTest {

    @SuppressWarnings("unused")
    @Autowired
    TaskCompletionRepo completionRepo;
    private SessionID sessionID;

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
    public void findByAccountIDTest() {
        //values
        int expectedSize = 1;
        String expectedTask = "Eat some food";
        String expectedImage = "https://cdn.eftm.com/wp-content/uploads/2019/10/Screen-Shot-2019-10-15-at-2.13.23-pm.png";
        int expectedReward = 100;
        String expectedAccountID = Objects.requireNonNull(AccountManager.verifySession(sessionID)).getID();

        //actions
        TaskManager.postCompletedTask(
                sessionID, expectedTask, expectedImage, expectedReward);

        List<TaskCompletionRecord> records = completionRepo.findAllByAccountID(expectedAccountID);
        TaskCompletionRecord record = records.get(0);

        //assertion message
        String findByAccountIDMessage = "invalid account provided for given accountID";

        //assertion statements
        Assertions.assertEquals(expectedSize, records.size(), findByAccountIDMessage);
        Assertions.assertEquals(expectedTask, record.getTask(), findByAccountIDMessage);
        Assertions.assertEquals(expectedImage, record.getImage(), findByAccountIDMessage);
        Assertions.assertEquals(expectedAccountID, record.getAccountID(), findByAccountIDMessage);
    }
}