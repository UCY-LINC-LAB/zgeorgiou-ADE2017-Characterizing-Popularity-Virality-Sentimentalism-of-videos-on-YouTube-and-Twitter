import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.zgeorg03.core.YouTubeRequests;
import org.junit.Test;

/**
 * Created by zgeorg03 on 2/24/17.
 */
public class YouTubeRequestsTest {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Test
    public void testStaticAndDynamicData(){
        String video = "THhxSwdNVT8";
        YouTubeRequests request = new YouTubeRequests(video,"AIzaSyDnSWFkRmnODsd5u7jmOAr74FNl4mPj1KA");

        JsonObject staticData = request.getStaticData();
        JsonObject dynamicData = request.getDynamicData(staticData.get("channel_id").getAsString());

        System.out.println(gson.toJson(staticData));
        System.out.println(gson.toJson(dynamicData));
        JsonObject object = request.getLatestComments();
        System.out.println(gson.toJson(object));
        System.out.println(request.getCost());
    }
    @Test
    public void testComments(){
        String video = "THhxSwdNVT8";
        YouTubeRequests request = new YouTubeRequests(video,"AIzaSyDnSWFkRmnODsd5u7jmOAr74FNl4mPj1KA");
        JsonObject object = request.getLatestComments();
        System.out.println(gson.toJson(object));
        System.out.println(request.getCost());
    }
}
