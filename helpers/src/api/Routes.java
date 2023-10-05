package api;

public abstract class Routes {
    public static final String THREADPOOL = "/threadpool";
    public static final String XML = "/xml";
    public static final String THREADPOOL_QUEUE = THREADPOOL + "/queue";
    public static final String XML_LOAD = XML + "/load";
    public static final String XML_VALID = XML + "/valid";
    public static final String XML_VALID_WORLDS = XML_VALID + "/worlds";
    public static final String HISTORY = "/history";
    public static final String ALLOCATIONS_HISTORY = HISTORY + "/allocations";
    public static final String SIMULATIONS_HISTORY = HISTORY + "/simulations";

}
