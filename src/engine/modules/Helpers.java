package engine.modules;

import java.util.List;

public class Helpers {
    public static boolean validateUniqueName(List<String> names, String name) {
        return names.stream().noneMatch(element -> element.equals(name));
    }
}
