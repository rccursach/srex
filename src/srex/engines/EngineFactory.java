package srex.engines;

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

    public SearchEngine createEngine (engines e) {
        SearchEngine eng = null;
        switch (e) {
            case BING:
                eng = new BingSearchAPI();
                break;
            case GOOGLE:
                eng = new GoogleSearchAPI();
                break;
        }
        return eng;
    }
}
