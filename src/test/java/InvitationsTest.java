import backend.entities.Invitation;
import java.sql.Timestamp;

public class InvitationsTest {

//    Invitations iv = new Invitations("1023912", "10293812", new Timestamp(System.currentTimeMillis()));

    public static void main(String[] args) {
        Invitation iv = new Invitation("1023912", "10293812", new Timestamp(System.currentTimeMillis()));
        System.out.println(iv.getSenderID());
    }
}
