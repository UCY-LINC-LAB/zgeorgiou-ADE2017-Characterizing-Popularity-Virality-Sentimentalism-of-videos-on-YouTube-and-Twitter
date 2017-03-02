import com.mongodb.ServerAddress;
import com.zgeorg03.videoprocess.ProcessVideo;
import com.zgeorg03.database.DBConnection;
import com.zgeorg03.database.DBServices;
import org.bson.Document;
import org.junit.Test;

import java.util.List;

/**
 * Created by zgeorg03 on 3/2/17.
 */
public class DBServicesTest {

    private final DBServices services;

    public DBServicesTest() {
        DBConnection connection = new DBConnection("test",new ServerAddress("localhost"));
        services = new DBServices(connection);
    }



    @Test
    public void getListUnprocessedVideos(){
        System.out.println(services.getFinishedButNotProcessedVideos(10));
    }
    @Test
    public void proccessVideo(){
        String videoID = "2Nnz0aaVyY4";
        ProcessVideo processVideo= new ProcessVideo(services.getProcessVideosDBService(), videoID);
    }
}
