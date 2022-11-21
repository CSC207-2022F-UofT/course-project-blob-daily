package controller;

import com.backend.QuestpetsApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = QuestpetsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskCompletionControllerTest {

    @BeforeEach
    public void setup() {

    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void test() {
        Assertions.assertTrue(true);
    }
}