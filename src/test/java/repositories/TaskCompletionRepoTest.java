package repositories;

import com.backend.QuestPetsApplication;
import com.backend.controller.TaskCompletionController;
import com.backend.entities.IDs.AccountID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.TaskActive;
import com.backend.entities.TaskCompletionRecord;
import com.backend.repositories.TaskActiveRepo;
import com.backend.repositories.TaskCompletionRepo;
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
    TaskCompletionController completionController;
    TaskManager taskManager;
    TaskActiveRepo taskActiveRepo;

    @Autowired
    public TaskCompletionRepoTest(TaskCompletionController completionController, TaskManager taskManager, TaskActiveRepo taskActiveRepo) {
        this.completionController = completionController;
        this.taskManager = taskManager;
        this.taskActiveRepo = taskActiveRepo;
    }

    @Autowired
    TaskCompletionRepo completionRepo;
    private AccountID accountID;
    private SessionID sessionID;

    @BeforeEach
    public void setup() {
        String username = "gochugum";
        String password = "abc123!";
        sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(AccountManager.loginAccount(username, password).getBody())).get("sessionID"));
        accountID = AccountManager.verifySession(sessionID);
    }

    @AfterEach
    public void tearDown() {
        taskManager.deleteAllCorrelatedCompletions(accountID);
        AccountManager.logoutAccount(sessionID);
    }

    @Test
    public void findByAccountIDTest() {
        //values
        TaskActive active = taskActiveRepo.findAll().get(0);
        int expectedSize = 1;
        String expectedTask = active.getName();
        String expectedImage = "https://cdn.eftm.com/wp-content/uploads/2019/10/Screen-Shot-2019-10-15-at-2.13.23-pm.png";
        String expectedAccountID = Objects.requireNonNull(AccountManager.verifySession(sessionID)).getID();

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