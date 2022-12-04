package entities;

import com.backend.entities.IDs.AccountID;
import com.backend.entities.TaskCompletionRecord;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


public class TaskCompletionRecordTest {
    private TaskCompletionRecord complete;

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

        Date expectedDate = new Date(System.currentTimeMillis());
        String expectedName = "play league";
        String expectedImage = "https://cdn.eftm.com/wp-content/uploads/2019/10/Screen-Shot-2019-10-15-at-2.13.23-pm.png";

        //action
        complete = new TaskCompletionRecord(expectedAccountIDObj, expectedDate, expectedName, expectedImage);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        Assertions.assertEquals(expectedAccountIDObj, complete.getAccountIDObject(), getConstructorMessage);
        Assertions.assertEquals(expectedAccountIDObj.getID(), complete.getAccountID(), getConstructorMessage);
        Assertions.assertEquals(expectedDate.toString().substring(0,10), complete.getDate(), getConstructorMessage);
        Assertions.assertEquals(expectedName, complete.getName(), getConstructorMessage);
        Assertions.assertEquals(expectedImage, complete.getImage());
    }

    @Test
    public void testTaskCompletionRecordConstructorAccountID() {
        //expected values
        AccountID expectedAccountIDObj = new AccountID(null);
        expectedAccountIDObj.generateID();

        String expectedAccountID = expectedAccountIDObj.getID();
        Date expectedDate = new Date(System.currentTimeMillis());
        String expectedName = "play league";
        String expectedImage = "https://cdn.eftm.com/wp-content/uploads/2019/10/Screen-Shot-2019-10-15-at-2.13.23-pm.png";

        //action
        complete = new TaskCompletionRecord(expectedAccountID, expectedDate, expectedName, expectedImage);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        assertThat(expectedAccountIDObj).usingRecursiveComparison().isEqualTo(complete.getAccountIDObject());
        Assertions.assertEquals(expectedAccountID, complete.getAccountID(), getConstructorMessage);
        Assertions.assertEquals(expectedDate.toString().substring(0,10), complete.getDate(), getConstructorMessage);
        Assertions.assertEquals(expectedName, complete.getName(), getConstructorMessage);
        Assertions.assertEquals(expectedImage, complete.getImage());
    }

    @Test
    public void testGetAccountIDObject() {
        //expected values
        AccountID expectedAccountIDObj = new AccountID(null);
        expectedAccountIDObj.generateID();

        Date expectedDate = new Date(System.currentTimeMillis());
        String expectedName = "play league";
        String expectedImage = "https://cdn.eftm.com/wp-content/uploads/2019/10/Screen-Shot-2019-10-15-at-2.13.23-pm.png";

        //action
        complete = new TaskCompletionRecord(expectedAccountIDObj, expectedDate, expectedName, expectedImage);

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
        Date expectedDate = new Date(System.currentTimeMillis());
        String expectedName = "play league";
        String expectedImage = "https://cdn.eftm.com/wp-content/uploads/2019/10/Screen-Shot-2019-10-15-at-2.13.23-pm.png";

        //action
        complete = new TaskCompletionRecord(expectedAccountID, expectedDate, expectedName, expectedImage);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        Assertions.assertEquals(expectedAccountID, complete.getAccountID(), getConstructorMessage);
    }

    @Test
    public void testGetDate() {
        //expected values
        AccountID expectedAccountIDObj = new AccountID(null);
        expectedAccountIDObj.generateID();

        String expectedAccountID = expectedAccountIDObj.getID();
        Date expectedDate = new Date(System.currentTimeMillis());
        String expectedName = "play league";
        String expectedImage = "https://cdn.eftm.com/wp-content/uploads/2019/10/Screen-Shot-2019-10-15-at-2.13.23-pm.png";

        //action
        complete = new TaskCompletionRecord(expectedAccountID, expectedDate, expectedName, expectedImage);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        Assertions.assertEquals(expectedDate.toString().substring(0,10), complete.getDate(), getConstructorMessage);
    }

    @Test
    public void testGetTask() {
        //expected values
        AccountID expectedAccountIDObj = new AccountID(null);
        expectedAccountIDObj.generateID();

        String expectedAccountID = expectedAccountIDObj.getID();
        Date expectedDate = new Date(System.currentTimeMillis());
        String expectedName = "play league";
        String expectedImage = "https://cdn.eftm.com/wp-content/uploads/2019/10/Screen-Shot-2019-10-15-at-2.13.23-pm.png";

        //action
        complete = new TaskCompletionRecord(expectedAccountID, expectedDate, expectedName, expectedImage);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        Assertions.assertEquals(expectedName, complete.getName(), getConstructorMessage);
    }

    @Test
    public void testGetImage() {
        //expected values
        AccountID expectedAccountIDObj = new AccountID(null);
        expectedAccountIDObj.generateID();

        String expectedAccountID = expectedAccountIDObj.getID();
        Date expectedDate = new Date(System.currentTimeMillis())    ;
        String expectedName = "play league";
        String expectedImage = "https://cdn.eftm.com/wp-content/uploads/2019/10/Screen-Shot-2019-10-15-at-2.13.23-pm.png";

        //action
        complete = new TaskCompletionRecord(expectedAccountID, expectedDate, expectedName, expectedImage);

        //assert message
        String getConstructorMessage = "Given parameters does not match expected parameters";

        //asserts
        Assertions.assertEquals(expectedImage, complete.getImage(), getConstructorMessage);
    }
}