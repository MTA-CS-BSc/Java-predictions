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
    public static final String APPROVE_REQUEST = ALLOCATIONS_HISTORY + "/approve";
    public static final String DECLINE_REQUEST = ALLOCATIONS_HISTORY + "/decline";
    public static final String SIMULATIONS_HISTORY = HISTORY + "/simulations";
    public static final String RESULTS = "/results";
    public static final String FINISHED_STATS = RESULTS + "/stats";
    public static final String PROPERTY_STATS = FINISHED_STATS + "/property";
    public static final String PROPERTY_CONSISTENCY = PROPERTY_STATS + "/consistency";
    public static final String PROPERTY_AVG = PROPERTY_STATS + "/avg";
    public static final String ENTITITY_COUNT_FOR_PROP = PROPERTY_STATS + "/entity/count";
    public static final String ENTITIES_AMOUNTS_PER_TICK = RESULTS + "/entities/amounts/pertick";
    public static final String SIMULATION = "/simulation";
    public static final String RUN_SIMULATION = SIMULATION + "/run";
    public static final String CREATE_SIMULATION = SIMULATION + "/create";
    public static final String CLONE_SIMULATION = SIMULATION + "/clone";
    public static final String STOP_SIMULATION = SIMULATION + "/stop";
    public static final String RESUME_SIMULATION = SIMULATION + "/resume";
    public static final String PAUSE_SIMULATION = SIMULATION + "/pause";

}
