package usecases.managers;

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
public class TaskManagerTest {
    private SessionID sessionID;
    private AccountID accountID;
    private String task;
    private final String image = "https://www.saycampuslife.com/wp-content/uploads/2017/10/collegelectures-800x500_c.jpg";
    private double reward;

    private final TaskManager taskManager;
    private final TaskActiveRepo taskActiveRepo;
    private final TaskCompletionRepo taskCompletionRepo;
    private final AccountSystemFacade accountSystemFacade;
    private final AccountManager accountManager;

    @Autowired
    public TaskManagerTest(TaskManager taskManager, TaskActiveRepo taskActiveRepo, TaskCompletionRepo taskCompletionRepo, AccountSystemFacade accountSystemFacade, AccountManager accountManager) {
        this.taskManager = taskManager;
        this.taskActiveRepo = taskActiveRepo;
        this.taskCompletionRepo = taskCompletionRepo;
        this.accountSystemFacade = accountSystemFacade;
        this.accountManager = accountManager;
    }

    @BeforeEach
    public void setup() {
        String username = "gochugum";
        String password = "abc123!";
        TaskActive active = taskActiveRepo.findAll().get(0);

        sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(username, password).getBody())).get("sessionID"));
        accountID = this.accountManager.verifySession(sessionID);
        task = active.getName();
        reward = active.getReward();
    }

    @AfterEach
    public void tearDown() {
        taskManager.deleteAllCorrelatedCompletions(accountID);
        this.accountSystemFacade.logoutAccount(sessionID);
    }

    @Test
    public void completeTaskTest(){
        //action
        TaskCompletionRecord expectedRecord = taskManager.completeTask(accountID, task, image);

        //assertion message
        String completeTaskMessage = "Unable finish task";

        //assertion statement
        Assertions.assertTrue(taskCompletionRepo.existsById(expectedRecord.getID()), completeTaskMessage);
    }

    @Test
    public void verifyTaskTest(){
        //action
        boolean expectedOutput = taskManager.verifyTask(task, reward);

        //assertion message
        String completeTaskMessage = "Unable finish task since it does not exist";

        //assertion statement
        Assertions.assertTrue(expectedOutput, completeTaskMessage);
    }

    @Test
    public void verifyTaskInvalidNameTest(){
        //action
        boolean expectedOutput = taskManager.verifyTask("bad name", reward);

        //assertion message
        String completeTaskMessage = "Unable finish task since it does not exist";

        //assertion statement
        Assertions.assertFalse(expectedOutput, completeTaskMessage);
    }

    @Test
    public void verifyTaskInvalidRewardTest(){
        //action
        boolean expectedOutput = taskManager.verifyTask(task, 10000);

        //assertion message
        String completeTaskMessage = "Unable finish task since it does not exist";

        //assertion statement
        Assertions.assertFalse(expectedOutput, completeTaskMessage);
    }

    @Test
    public void deleteAllCorrelatedCompletionsTest(){
        //setup
        int expectedBefore = 1;
        int expectedAfter = 0;

        //actions
        taskManager.completeTask(accountID, task, image);
        List<TaskCompletionRecord> before = taskManager.getTaskCompletionRecords(accountID);
        taskManager.deleteAllCorrelatedCompletions(accountID);
        List<TaskCompletionRecord> after = taskManager.getTaskCompletionRecords(accountID);

        //assertion message
        String deleteAllMessage = "invalid account provided for given accountID";

        //assertion statements
        Assertions.assertEquals(expectedBefore, before.size(), deleteAllMessage);
        Assertions.assertEquals(expectedAfter, after.size(), deleteAllMessage);
    }

    @Test
    public void getTaskCompletionRecordsTest(){
        //setup
        int expectedAmount = 1;

        //actions
        taskManager.completeTask(accountID, task, image);
        List<TaskCompletionRecord> records = taskManager.getTaskCompletionRecords(accountID);

        //assertion message
        String deleteAllMessage = "invalid account provided for given accountID";

        //assertion statements
        Assertions.assertEquals(expectedAmount, records.size(), deleteAllMessage);
    }

    @Test
    public void checkCompletedTest(){
        //setup
        boolean expectedOutput = taskManager.checkCompleted(task, accountID);

        //assertion message
        String completeTaskMessage = "Unable finish task as it is already completed";

        //assertion statement
        Assertions.assertFalse(expectedOutput, completeTaskMessage);
    }

    @Test
    public void checkCompletedAlreadyCompletedTest(){
        //setup
        taskManager.completeTask(accountID, task, image);
        boolean expectedOutput = taskManager.checkCompleted(task, accountID);

        //assertion message
        String completeTaskMessage = "Unable finish task as it is already completed";

        //assertion statement
        Assertions.assertTrue(expectedOutput, completeTaskMessage);
    }

    @Test
    public void getActiveTasksUpdateTest(){
        //setup
        int expectedAmount = 3;
        List<TaskActive> taskActives = taskActiveRepo.findAll();

        //actions
        List<TaskActive> updatedTaskActives = taskManager.updateActiveTasks(accountID, taskActives);

        //assertion message
        String activeTaskMessage = "active tasks could not be updated";

        //assert
        Assertions.assertEquals(expectedAmount, updatedTaskActives.size(), activeTaskMessage);
    }

    @Test
    public void getActiveTasksUpdateCompletedTaskTest(){
        //setup
        int expectedAmount = 2;
        List<TaskActive> taskActives = taskActiveRepo.findAll();

        //actions
        taskManager.completeTask(accountID, task, image);
        List<TaskActive> updatedTaskActives = taskManager.updateActiveTasks(accountID, taskActives);

        //assertion message
        String activeTaskMessage = "active tasks could not be updated";

        //assert
        Assertions.assertEquals(expectedAmount, updatedTaskActives.size(), activeTaskMessage);
    }

    @Test
    public void getActiveTasksNewTest(){
        //setup
        int expectedAmount = 3;
        List<TaskActive> taskActives = taskManager.newActiveTasks();

        //assertion message
        String activeTaskMessage = "active tasks could not be replaced";

        //assert
        Assertions.assertEquals(expectedAmount, taskActives.size(), activeTaskMessage);
    }
}