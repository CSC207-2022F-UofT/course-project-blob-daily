package entities;

import com.backend.entities.TaskActive;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

public class TaskActiveTest {
    TaskActive active;

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
        String expectedTimestamp = new Timestamp(System.currentTimeMillis()).toString();

        //action
        active = new TaskActive(expectedName, expectedReward, expectedTimestamp);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        Assertions.assertEquals(expectedName, active.getName(), getConstructorMessage);
        Assertions.assertEquals(expectedReward, active.getReward(), getConstructorMessage);
        Assertions.assertEquals(expectedTimestamp, active.getTimestamp(), getConstructorMessage);
    }

    @Test
    public void testGetName() {
        //expected values
        String expectedName = "play league";
        double expectedReward = 10;
        String expectedTimestamp = new Timestamp(System.currentTimeMillis()).toString();

        //action
        active = new TaskActive(expectedName, expectedReward, expectedTimestamp);

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
        String expectedTimestamp = new Timestamp(System.currentTimeMillis()).toString();

        //action
        active = new TaskActive(expectedName, expectedReward, expectedTimestamp);

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
        String expectedTimestamp = new Timestamp(System.currentTimeMillis()).toString();

        //action
        active = new TaskActive(expectedName, expectedReward, expectedTimestamp);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        Assertions.assertEquals(expectedTimestamp, active.getTimestamp(), getConstructorMessage);
    }
}
