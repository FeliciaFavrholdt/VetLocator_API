package dat;

import dat.config.ApplicationConfig;

public class Main {

    public static void main(String[] args) {
        ApplicationConfig.startServer(7071);
        System.out.println("Hello World");
    }
}