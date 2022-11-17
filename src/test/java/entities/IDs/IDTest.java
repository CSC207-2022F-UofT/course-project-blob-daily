package entities.IDs;

import com.backend.entities.IDs.ID;
import com.backend.entities.IDs.SessionID;
import com.backend.entities.criteria.Criteria;
import com.backend.entities.criteria.conditions.ContainsAtleastTypeExpression;
import com.backend.entities.criteria.conditions.SizeExpression;
import com.backend.error.handlers.LogHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class IDTest {
    ID id;

    @BeforeEach
    public void setUp() {
        LogHandler.DEPRECATED = true;
    }

    @AfterEach
    public void tearDown() {
        LogHandler.DEPRECATED = false;
    }

    @Test
    public void testConstructor() {
        // Expected values
        String expectedDefaultID = "p0LkdlajijdILD89";
        Criteria expectedCriteria = new Criteria(List.of(new SizeExpression(16, expectedDefaultID)));

        // Action
        id = new ID(expectedDefaultID, expectedCriteria);
        String actualID = id.getID();

        // Assert messages
        String getConstructorMessage = String.format("The given ID %s was returned instead of the expect %s",
                actualID, expectedDefaultID);

        // Asserts
        Assertions.assertEquals(actualID, expectedDefaultID, getConstructorMessage);
    }

    @Test
    public void testGetID() {
        // Expected values
        String expectedDefaultID = "p0LkdlajijdILD89";
        Criteria expectedCriteria = new Criteria(List.of(new SizeExpression(16, expectedDefaultID)));

        // Action
        id = new ID(expectedDefaultID, expectedCriteria);
        String actualID = id.getID();

        // Assert messages
        String getIDMessage = String.format("The given ID %s was returned instead of the expect %s",
                actualID, expectedDefaultID);

        // Asserts
        Assertions.assertEquals(actualID, expectedDefaultID, getIDMessage);
    }

    @Test
    public void testIsValid() {
        // Expected values
        String expectedDefaultID = "p0LkdlajijdILD89";
        Criteria expectedCriteria = new Criteria(List.of(new SizeExpression(16, expectedDefaultID)));

        // Action
        id = new ID(expectedDefaultID, expectedCriteria);

        // Assert messages
        String getValidMessage = String.format("The given ID %s was unexpectedly found invalid",
                expectedDefaultID);

        // Asserts
        Assertions.assertTrue(id.isValid(), getValidMessage);
    }

    @Test
    public void testInvalidID() {
        // Expected values
        String invalidDefaultID = "ad";
        Criteria expectedCriteria = new Criteria(List.of(new SizeExpression(16, invalidDefaultID)));

        // Action
        id = new ID(invalidDefaultID, expectedCriteria);

        // Assert messages
        String invalidSessionIDMessage = String.format("An expected invalid ID %s was found to be valid",
                invalidDefaultID);

        // Asserts
        Assertions.assertFalse(id.isValid(), invalidSessionIDMessage);
    }

    @Test
    public void testGetCriteria() {
        // Expected values
        String expectedDefaultID = "p0LkdlajijdILD89";
        Criteria expectedCriteria = new Criteria(List.of(new SizeExpression(16, expectedDefaultID)));

        // Action
        id = new ID(expectedDefaultID, expectedCriteria);
        Criteria actualCriteria = id.getCriteria();

        // Assert messages
        String getCriteriaMessage = String.format("The return criteria %s doesn't match the expected criteria %s",
                actualCriteria, expectedCriteria);

        // Asserts
        Assertions.assertEquals(actualCriteria, expectedCriteria, getCriteriaMessage);
    }

    @Test
    public void testGenerateID() {
        // Expected values
        Criteria expectedCriteria = new Criteria(List.of(new SizeExpression(16, null), new ContainsAtleastTypeExpression(new ArrayList<>(List.of("letter")), null)));

        // Action
        id = new SessionID(null);
        id.generateID();

        // Assert messages
        String generateIDMessage = String.format("The generated ID %s is not valid",
                id.getID());

        // Asserts
        Assertions.assertTrue(id.isValid(), generateIDMessage);
    }
}
