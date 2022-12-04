package entities;

import com.backend.entities.Task;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskTest {
    private Task task;

    @BeforeEach
    public void setUp() {
        LogHandler.DEPRECATED = true;
    }

    @AfterEach
    public void tearDown() {
        LogHandler.DEPRECATED = false;
    }

    @Test
    public void testTaskConstructor() {
        //expected values
        String expectedName = "play league";
        double expectedReward = 10;

        //action
        task = new Task(expectedName, expectedReward);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        Assertions.assertEquals(expectedName, task.getName(), getConstructorMessage);
        Assertions.assertEquals(expectedReward, task.getReward(), getConstructorMessage);
    }

    @Test
    public void testGetName() {
        //expected values
        String expectedName = "play league";
        double expectedReward = 10;

        //action
        task = new Task(expectedName, expectedReward);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        Assertions.assertEquals(expectedName, task.getName(), getConstructorMessage);
    }

    @Test
    public void testGetReward() {
        //expected values
        String expectedName = "play league";
        double expectedReward = 10;

        //action
        task = new Task(expectedName, expectedReward);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        Assertions.assertEquals(expectedReward, task.getReward(), getConstructorMessage);
    }
}