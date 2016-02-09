
import srex.engines.EngineFactory;
import srex.engines.SearchEngine;
import srex.models.Query;

/**
 * Created by rccursach on 1/29/16.
 */
public class main {

    public static void main(String[] args)
    {
        Query q = new Query("perro");
        SearchEngine motor;
        EngineFactory ef = EngineFactory.getInstance();

        motor = ef.createEngine(EngineFactory.engines.BING );
        motor.initSearch(q);
    }
}
