package controller;

import com.backend.QuestPetsApplication;
import com.backend.controller.TaskActiveController;
import com.backend.entities.IDs.SessionID;
import com.backend.usecases.managers.AccountManager;
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

    @Autowired
    TaskActiveController activeController;
    private SessionID sessionID;

    @BeforeEach
    public void setup() {
        String username = "username";
        String password = "abc123!";
        sessionID = new SessionID((String) ((JSONObject) Objects.requireNonNull(AccountManager.loginAccount(username, password).getBody())).get("sessionID"));
    }

    @AfterEach
    public void tearDown() {
        AccountManager.logoutAccount(sessionID);
    }

    @Test
    public void getActiveTasksTest() {
        //value
        HttpStatus expectedStatus = HttpStatus.OK;

        //action
        ResponseEntity<?> actualResponse = activeController.getActiveTasks(sessionID.toString());

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
        ResponseEntity<?> actualResponse = activeController.getActiveTasks("bad ID");

        //assertion message
        String activeTaskMessage = "Unable to get active tasks due to invalid session";

        //assertion statement
        Assertions.assertEquals(expectedStatus, actualResponse.getStatusCode(), activeTaskMessage);
    }
}