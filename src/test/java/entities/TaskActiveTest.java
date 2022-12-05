package entities;

import com.backend.entities.TaskActive;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

public class TaskActiveTest {
    private TaskActive active;

    @BeforeEach
    public void setUp() {
        LogHandler.DEPRECATED = true;
    }

    @AfterEach
    public void tearDown() {
        LogHandler.DEPRECATED = false;
    }

    @Test
    public void testTaskActiveConstructor() {
        //expected values
        String expectedName = "play league";
        double expectedReward = 10;
        Date expectedDate = new Date(System.currentTimeMillis());

        //action
        active = new TaskActive(expectedName, expectedReward, expectedDate);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        Assertions.assertEquals(expectedName, active.getName(), getConstructorMessage);
        Assertions.assertEquals(expectedReward, active.getReward(), getConstructorMessage);
        Assertions.assertEquals(expectedDate.toString().substring(0,10), active.getDate(), getConstructorMessage);
    }

    @Test
    public void testGetName() {
        //expected values
        String expectedName = "play league";
        double expectedReward = 10;
        Date expectedDate = new Date(System.currentTimeMillis());

        //action
        active = new TaskActive(expectedName, expectedReward, expectedDate);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        Assertions.assertEquals(expectedName, active.getName(), getConstructorMessage);
    }

    @Test
    public void testGetReward() {
        //expected values
        String expectedName = "play league";
        double expectedReward = 10;
        Date expectedDate = new Date(System.currentTimeMillis());

        //action
        active = new TaskActive(expectedName, expectedReward, expectedDate);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        Assertions.assertEquals(expectedReward, active.getReward(), getConstructorMessage);
    }

    @Test
    public void testGetTimeStamp() {
        //expected values
        String expectedName = "play league";
        double expectedReward = 10;
        Date expectedDate = new Date(System.currentTimeMillis());

        //action
        active = new TaskActive(expectedName, expectedReward, expectedDate);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        Assertions.assertEquals(expectedDate.toString().substring(0,10), active.getDate(), getConstructorMessage);
    }
}