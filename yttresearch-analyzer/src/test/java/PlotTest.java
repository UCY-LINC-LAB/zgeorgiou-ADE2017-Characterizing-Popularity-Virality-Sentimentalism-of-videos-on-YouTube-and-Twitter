import com.zgeorg03.core.PlotProducer;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zgeorg03 on 3/11/17.
 */
public class PlotTest {

    @Test
    public void testPlot(){

        PlotProducer plotProducer = new PlotProducer("/tmp/thesis/gnuplot");
        List<String> input = Arrays.asList(
                "plt.plot([0,1,2],[1,5,8],label='popular')"
        );
        String result  = plotProducer.producePLot("f3",input,"Test");
        System.out.println(result);
    }
}
