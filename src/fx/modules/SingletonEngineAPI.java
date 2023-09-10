package fx.modules;

import engine.EngineAPI;

public abstract class SingletonEngineAPI {
    public static final EngineAPI api = new EngineAPI();
}
