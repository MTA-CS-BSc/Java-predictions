package ui.handlers;

import engine.EngineAPI;
import engine.consts.PropTypes;
import engine.prototypes.PropertyDTO;
import ui.consts.Constants;
import ui.modules.ScanCycles;
import ui.printers.EnvPropsInitializerPrinter;
import ui.scanners.EnvPropsInitializerScanner;

public class EnvPropsInitializerHandler extends EnvPropsInitializerScanner {
    public EnvPropsInitializerHandler() {
        super();
    }
    private void handleEnvSetSelection(EngineAPI api, String uuid, int selection) {
        String val = "";
        System.out.println("Enter value:");

        PropertyDTO prop = api.getEnvironmentProperties(uuid).get(selection - 1);

        switch (prop.getType()) {
            case PropTypes.BOOLEAN:
                val = ScanCycles.scanBoolean(scanner);
                break;
            case PropTypes.STRING:
                val = scanner.nextLine();
                break;
            case PropTypes.DECIMAL:
                val = ScanCycles.scanDecimal(scanner);
                break;
            case PropTypes.FLOAT:
                val = ScanCycles.scanFloat(scanner);
                break;
        }

        api.setEnvironmentVariable(uuid, prop, val);
    }
    public boolean printVarsAndHandleSelectedPropChange(EngineAPI api, String uuid) {
        System.out.println("Available env vars to set: ");
        int propsAmount = EnvPropsInitializerPrinter.printEnvironmentProps(api, uuid);
        int selection = ScanCycles.scanOption(scanner, propsAmount + 1);

        while (selection == Constants.NOT_FOUND)
            selection = ScanCycles.scanOption(scanner, propsAmount + 1);

        if (selection == propsAmount + 1)
            return false;

        handleEnvSetSelection(api, uuid, selection);
        return true;
    }
    public void handlePropsSettings(EngineAPI api, String uuid) {
        while (printVarsAndHandleSelectedPropChange(api, uuid));
    }
}
