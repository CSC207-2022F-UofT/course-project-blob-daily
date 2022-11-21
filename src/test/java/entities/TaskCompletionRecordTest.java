package entities;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.TaskCompletionRecord;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;

public class TaskCompletionRecordTest {
    TaskCompletionRecord complete;

    @BeforeEach
    public void setUp() {
        LogHandler.DEPRECATED = true;
    }

    @AfterEach
    public void tearDown() {
        LogHandler.DEPRECATED = false;
    }

    @Test
    public void testTaskCompletionRecordConstructorAccountIDObject() {
        //expected values
        AccountID expectedAccountIDObj = new AccountID(null);
        expectedAccountIDObj.generateID();

        String expectedTimestamp = new Timestamp(System.currentTimeMillis()).toString();
        String expectedName = "play league";
        String expectedImage = "https://cdn.eftm.com/wp-content/uploads/2019/10/Screen-Shot-2019-10-15-at-2.13.23-pm.png";

        //action
        complete = new TaskCompletionRecord(expectedAccountIDObj, expectedTimestamp, expectedName, expectedImage);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        Assertions.assertEquals(expectedAccountIDObj, complete.getAccountIDObject(), getConstructorMessage);
        Assertions.assertEquals(expectedAccountIDObj.getID(), complete.getAccountID(), getConstructorMessage);
        Assertions.assertEquals(expectedTimestamp, complete.getTimestamp(), getConstructorMessage);
        Assertions.assertEquals(expectedName, complete.getTask(), getConstructorMessage);
        Assertions.assertEquals(expectedImage, complete.getImage());
    }

    @Test
    public void testTaskCompletionRecordConstructorAccountID() {
        //expected values
        AccountID expectedAccountIDObj = new AccountID(null);
        expectedAccountIDObj.generateID();

        String expectedAccountID = expectedAccountIDObj.getID();
        String expectedTimestamp = new Timestamp(System.currentTimeMillis()).toString();
        String expectedName = "play league";
        String expectedImage = "https://cdn.eftm.com/wp-content/uploads/2019/10/Screen-Shot-2019-10-15-at-2.13.23-pm.png";

        //action
        complete = new TaskCompletionRecord(expectedAccountID, expectedTimestamp, expectedName, expectedImage);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        assertThat(expectedAccountIDObj).usingRecursiveComparison().isEqualTo(complete.getAccountIDObject());
        Assertions.assertEquals(expectedAccountID, complete.getAccountID(), getConstructorMessage);
        Assertions.assertEquals(expectedTimestamp, complete.getTimestamp(), getConstructorMessage);
        Assertions.assertEquals(expectedName, complete.getTask(), getConstructorMessage);
        Assertions.assertEquals(expectedImage, complete.getImage());
    }

    @Test
    public void testGetAccountIDObject() {
        //expected values
        AccountID expectedAccountIDObj = new AccountID(null);
        expectedAccountIDObj.generateID();

        String expectedTimestamp = new Timestamp(System.currentTimeMillis()).toString();
        String expectedName = "play league";
        String expectedImage = "https://cdn.eftm.com/wp-content/uploads/2019/10/Screen-Shot-2019-10-15-at-2.13.23-pm.png";

        //action
        complete = new TaskCompletionRecord(expectedAccountIDObj, expectedTimestamp, expectedName, expectedImage);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        Assertions.assertEquals(expectedAccountIDObj, complete.getAccountIDObject(), getConstructorMessage);
    }

    @Test
    public void testGetAccountID() {
        //expected values
        AccountID expectedAccountIDObj = new AccountID(null);
        expectedAccountIDObj.generateID();

        String expectedAccountID = expectedAccountIDObj.getID();
        String expectedTimestamp = new Timestamp(System.currentTimeMillis()).toString();
        String expectedName = "play league";
        String expectedImage = "https://cdn.eftm.com/wp-content/uploads/2019/10/Screen-Shot-2019-10-15-at-2.13.23-pm.png";

        //action
        complete = new TaskCompletionRecord(expectedAccountID, expectedTimestamp, expectedName, expectedImage);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        Assertions.assertEquals(expectedAccountID, complete.getAccountID(), getConstructorMessage);
    }

    @Test
    public void testGetTimestamp() {
        //expected values
        AccountID expectedAccountIDObj = new AccountID(null);
        expectedAccountIDObj.generateID();

        String expectedAccountID = expectedAccountIDObj.getID();
        String expectedTimestamp = new Timestamp(System.currentTimeMillis()).toString();
        String expectedName = "play league";
        String expectedImage = "https://cdn.eftm.com/wp-content/uploads/2019/10/Screen-Shot-2019-10-15-at-2.13.23-pm.png";

        //action
        complete = new TaskCompletionRecord(expectedAccountID, expectedTimestamp, expectedName, expectedImage);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        Assertions.assertEquals(expectedTimestamp, complete.getTimestamp(), getConstructorMessage);
    }

    @Test
    public void testGetTask() {
        //expected values
        AccountID expectedAccountIDObj = new AccountID(null);
        expectedAccountIDObj.generateID();

        String expectedAccountID = expectedAccountIDObj.getID();
        String expectedTimestamp = new Timestamp(System.currentTimeMillis()).toString();
        String expectedName = "play league";
        String expectedImage = "https://cdn.eftm.com/wp-content/uploads/2019/10/Screen-Shot-2019-10-15-at-2.13.23-pm.png";

        //action
        complete = new TaskCompletionRecord(expectedAccountID, expectedTimestamp, expectedName, expectedImage);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        Assertions.assertEquals(expectedName, complete.getTask(), getConstructorMessage);
    }

    @Test
    public void testGetImage() {
        //expected values
        AccountID expectedAccountIDObj = new AccountID(null);
        expectedAccountIDObj.generateID();

        String expectedAccountID = expectedAccountIDObj.getID();
        String expectedTimestamp = new Timestamp(System.currentTimeMillis()).toString();
        String expectedName = "play league";
        String expectedImage = "https://cdn.eftm.com/wp-content/uploads/2019/10/Screen-Shot-2019-10-15-at-2.13.23-pm.png";

        //action
        complete = new TaskCompletionRecord(expectedAccountID, expectedTimestamp, expectedName, expectedImage);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        Assertions.assertEquals(expectedImage, complete.getImage(), getConstructorMessage);
    }
}
