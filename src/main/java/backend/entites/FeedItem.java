package backend.entites;

public class FeedItem {
    //Attributes
    private ProtectedAccount account;
    private Avatar avatar;
    private TaskCompletionRecord record;

    // Constructor
    public FeedItem(ProtectedAccount acccount, TaskCompletionRecord record, Avatar avatar){
        this.account = acccount;
        this.record = record;
        this.avatar = avatar;
    }
}
