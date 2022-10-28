import backend.entites.Invitations;
import java.sql.Timestamp;
import org.junit.Before;
import org.junit.Test;

public class InvitationsTest {

//    Invitations iv = new Invitations("1023912", "10293812", new Timestamp(System.currentTimeMillis()));

    public static void main(String[] args) {
        Invitations iv = new Invitations("1023912", "10293812", new Timestamp(System.currentTimeMillis()));
        System.out.println(iv.getSenderID());
    }
}
