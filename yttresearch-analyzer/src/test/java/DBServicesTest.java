import com.mongodb.ServerAddress;
import com.zgeorg03.analysis.SentimentAnalysis;
import com.zgeorg03.database.DBConnection;
import com.zgeorg03.database.DBServices;
import com.zgeorg03.rawvideos.ProcessVideo;
import org.junit.Test;

/**
 * Created by zgeorg03 on 3/2/17.
 */
public class DBServicesTest {

    private final DBServices services;


    public DBServicesTest() {
        DBConnection connection = new DBConnection("yttresearch",new ServerAddress("localhost"));
        services = new DBServices(connection);
    }



    /**
    @Test
    public void getListUnprocessedVideos(){
        System.out.println(services.getFinishedButNotProcessedVideos(10));
    }
    **/
    /**
    @Test
    public void proccessVideo() throws Exception {
        String videoID = "7aASbWJS-uM";
        SentimentAnalysis sentimentAnalysis = new SentimentAnalysis("scripts/sentiment/sentiment.py");
        ProcessVideo processVideo= new ProcessVideo(services, videoID, sentimentAnalysis);
        services.getProcessVideoDBService().addOrReplaceProcessedVideo(processVideo.getVideo());
    }
    **/
}
