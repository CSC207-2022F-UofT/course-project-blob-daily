package usecases;

import com.backend.QuestPetsApplication;
import com.backend.usecases.AccountManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = QuestPetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountManagerTest {

    @Test
    public void testLoginValid(){
        Assertions.assertTrue(true);
    }
}
