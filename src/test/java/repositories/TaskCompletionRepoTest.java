package repositories;

import com.backend.QuestPetsApplication;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.TaskActive;
import com.backend.entities.TaskCompletionRecord;
import com.backend.repositories.TaskActiveRepo;
import com.backend.repositories.TaskCompletionRepo;
import com.backend.usecases.facades.AccountSystemFacade;
import com.backend.usecases.managers.AccountManager;
import com.backend.usecases.managers.TaskManager;
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
    private final TaskManager taskManager;
    private final TaskActiveRepo taskActiveRepo;
    private final AccountSystemFacade accountSystemFacade;
    private final AccountManager accountManager;

    @Autowired
    public TaskCompletionRepoTest(TaskManager taskManager, TaskActiveRepo taskActiveRepo, AccountSystemFacade accountSystemFacade, AccountManager accountManager) {
        this.taskManager = taskManager;
        this.taskActiveRepo = taskActiveRepo;
        this.accountSystemFacade = accountSystemFacade;
        this.accountManager = accountManager;
    }

    @Autowired
    TaskCompletionRepo completionRepo;
    private AccountID accountID;
    private SessionID sessionID;

    @BeforeEach
    public void setup() {
        String username = "gochugum";
        String password = "abc123!";
        sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(username, password).getBody())).get("sessionID"));
        accountID = this.accountManager.verifySession(sessionID);
    }

    @AfterEach
    public void tearDown() {
        taskManager.deleteAllCorrelatedCompletions(accountID);
        this.accountSystemFacade.logoutAccount(sessionID);
    }

    @Test
    public void findByAccountIDTest() {
        //values
        TaskActive active = taskActiveRepo.findAll().get(0);
        int expectedSize = 1;
        String expectedTask = active.getName();
        String expectedImage = "https://cdn.eftm.com/wp-content/uploads/2019/10/Screen-Shot-2019-10-15-at-2.13.23-pm.png";
        String expectedAccountID = Objects.requireNonNull(this.accountManager.verifySession(sessionID)).getID();

        //actions
        taskManager.completeTask(accountID, expectedTask, expectedImage);

        List<TaskCompletionRecord> records = completionRepo.findAllByAccountID(expectedAccountID);
        TaskCompletionRecord record = records.get(0);

        //assertion message
        String findByAccountIDMessage = "invalid account provided for given accountID";

        //assertion statements
        Assertions.assertEquals(expectedSize, records.size(), findByAccountIDMessage);
        Assertions.assertEquals(expectedTask, record.getName(), findByAccountIDMessage);
        Assertions.assertEquals(expectedImage, record.getImage(), findByAccountIDMessage);
        Assertions.assertEquals(expectedAccountID, record.getAccountID(), findByAccountIDMessage);
    }
}