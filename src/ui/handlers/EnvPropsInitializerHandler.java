package ui.handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dtos.PropertyDTO;
import dtos.ResponseDTO;
import engine.EngineAPI;
import engine.consts.PropTypes;
import ui.consts.Constants;
import ui.modules.ScanCycles;
import ui.printers.EnvPropsInitializerPrinter;
import ui.scanners.EnvPropsInitializerScanner;

import java.util.List;

public class EnvPropsInitializerHandler extends EnvPropsInitializerScanner {
    public EnvPropsInitializerHandler() {
        super();
    }
    private void handleEnvSetSelection(EngineAPI api, String uuid, int selection) {
        List<PropertyDTO> props = new Gson().fromJson(api.getEnvironmentProperties(uuid).getData(), new TypeToken<List<PropertyDTO>>(){}.getType());
        PropertyDTO prop = props.get(selection - 1);

        String val = "";
        System.out.println("Enter value:");

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

        ResponseDTO setEnvPropResponse = api.setEnvironmentVariable(uuid, prop, val);

        if (setEnvPropResponse.getStatus() == 400)
            System.out.println(setEnvPropResponse.getErrorDescription().getCause());
    }
    public boolean printVarsAndHandleSelectedPropChange(EngineAPI api, String uuid) {
        System.out.println("Available environment variables to set: ");
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

        System.out.println("Set all requested variables. Environment variables that weren't set will be randomly assigned.");
    }
}
