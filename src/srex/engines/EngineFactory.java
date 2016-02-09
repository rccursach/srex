package srex.engines;

import srex.engines.implementations.BingSearchAPI;
import srex.engines.implementations.GoogleSearchAPI;
import srex.config.PropReader;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by rccursach on 1/29/16.
 */
public class EngineFactory {
    private static EngineFactory _instance;
    public enum engines { BING, GOOGLE };

    private EngineFactory() {
    }

    public static EngineFactory getInstance() {
        if( _instance == null ) {
            _instance = new EngineFactory();
        }
        return _instance;
    }

    public SearchEngine createEngine (engines en) {
        SearchEngine eng = null;
        PropReader p = null;

        try {
            switch (en) {
                case BING:
                    p = new PropReader("bing.properties");
                    eng = new BingSearchAPI(p.getValueFor("API_KEY"));
                    break;
                case GOOGLE:
                    p = new PropReader("google.properties");
                    eng = new GoogleSearchAPI(p.getValueFor("API_KEY"), p.getValueFor("API_CX"));
                    break;
            }
        }
        catch (IOException e) {
            System.err.println(Arrays.toString(e.getStackTrace()));
        }
        return eng;
    }
}
