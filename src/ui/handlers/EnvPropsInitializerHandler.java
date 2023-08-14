//package ui.handlers;
//
//import engine.SystemOrchestrator;
//import engine.consts.PropTypes;
//import engine.consts.Restrictions;
//import engine.prototypes.implemented.Property;
//import engine.prototypes.implemented.World;
//import ui.consts.Constants;
//import ui.logs.UILoggers;
//import ui.modules.Utils;
//import ui.printers.EnvPropsInitializerPrinter;
//import ui.scanners.EnvPropsInitializerScanner;
//
//import java.util.Comparator;
//import java.util.stream.Collectors;
//
//public class EnvPropsInitializerHandler extends EnvPropsInitializerScanner {
//    public EnvPropsInitializerHandler() {
//        super();
//    }
//    private void handleEnvSetSelection(World world, int selection) {
//        String val = "";
//        System.out.println("Enter value:");
//
//        Property prop = world.getEnvironment().getEnvVars().values()
//                .stream()
//                .sorted(Comparator.comparing(Property::getName))
//                .collect(Collectors.toList()).get(selection - 1);
//
//        switch (prop.getType()) {
//            case PropTypes.BOOLEAN:
//                val = Utils.scanBoolean(scanner);
//                break;
//            case PropTypes.STRING:
//                val = scanner.nextLine();
//                break;
//            case PropTypes.DECIMAL:
//                val = String.valueOf(Utils.scanOption(scanner, Restrictions.MAX_RANGE));
//                break;
//            case PropTypes.FLOAT:
//                val = Utils.scanFloat(scanner);
//                break;
//        }
//
//        prop.getValue().setRandomInitialize(false);
//        prop.getValue().setInit(val);
//        prop.getValue().setCurrentValue(prop.getValue().getInit());
//    }
//    public boolean printVarsAndHandleSelectedPropChange(World world) {
//        System.out.println("Available env vars to set: ");
//        int propsAmount = EnvPropsInitializerPrinter.printEnvs(world.getEnvironment());
//        int selection = Utils.scanOption(scanner, propsAmount + 1);
//
//        while (selection == Constants.NOT_FOUND)
//            selection = Utils.scanOption(scanner, propsAmount + 1);
//
//        if (selection == propsAmount + 1)
//            return false;
//
//        handleEnvSetSelection(world, selection);
//        return true;
//    }
//    public void handlePropsSettings(SystemOrchestrator so) {
//        while (printVarsAndHandleSelectedPropChange(so)) {
//            ;
//        }
//    }
//}
