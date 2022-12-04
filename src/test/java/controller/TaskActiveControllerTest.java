package controller;

import com.backend.QuestPetsApplication;
import com.backend.controller.TaskActiveController;
import com.backend.entities.IDs.SessionID;
import com.backend.usecases.facades.AccountSystemFacade;
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
public class TaskActiveControllerTest {
    private SessionID sessionID;

    private final TaskActiveController taskActiveController;
    private final AccountSystemFacade accountSystemFacade;

    @Autowired
    public TaskActiveControllerTest(TaskActiveController taskActiveController, AccountSystemFacade accountSystemFacade) {
        this.taskActiveController = taskActiveController;
        this.accountSystemFacade = accountSystemFacade;
    }

    @BeforeEach
    public void setup() {
        String username = "gochugum";
        String password = "abc123!";
        sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(this.accountSystemFacade.loginAccount(username, password).getBody())).get("sessionID"));
    }

    @AfterEach
    public void tearDown() {
        this.accountSystemFacade.logoutAccount(sessionID);
    }

    @Test
    public void getActiveTasksTest() {
        //value
        HttpStatus expectedStatus = HttpStatus.OK;

        //action
        ResponseEntity<?> actualResponse = this.taskActiveController.getActiveTasks(sessionID.toString());

        //assertion message
        String activeTaskMessage = "Unable to get active tasks due to invalid session";

        //assertion statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), activeTaskMessage);
    }

    @Test
    public void getActiveTasksInvalidTest() {
        //value
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

        //action
        ResponseEntity<?> actualResponse = this.taskActiveController.getActiveTasks("bad ID");

        //assertion message
        String activeTaskMessage = "Unable to get active tasks due to invalid session";

        //assertion statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), activeTaskMessage);
    }
}