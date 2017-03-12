import com.zgeorg03.classification.Features;
import org.junit.Test;

/**
 * Created by zgeorg03 on 3/12/17.
 */
public class FeaturesTest {

    @Test
    public void featuresTest(){
        Features features =new Features();
        features.add("test",03.3);

        System.out.println(features);
    }
}
