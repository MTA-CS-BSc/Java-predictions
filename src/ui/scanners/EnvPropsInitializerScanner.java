package ui.scanners;

import java.util.Scanner;

public abstract class EnvPropsInitializerScanner {
    protected Scanner scanner;

    public EnvPropsInitializerScanner() {
        scanner = new Scanner(System.in);
    }
}
