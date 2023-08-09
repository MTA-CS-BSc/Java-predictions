package ui.printers;

import com.sun.xml.internal.ws.util.StringUtils;
import ui.enums.MainMenu;

import java.util.Arrays;

public abstract class MainMenuPrinter {
    public static void print() {
        System.out.println("Please choose an option from the menu below:");
        System.out.println("---------------------------------------------");

        Arrays.stream(MainMenu.values())
                .forEach(element -> {
                    System.out.printf("%d -> %s\n", element.ordinal() + 1,
                            StringUtils.capitalize(element.name().replace("_", " ").toLowerCase()));
                });
    }
}
