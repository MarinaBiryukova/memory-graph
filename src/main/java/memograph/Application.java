package memograph;

import java.io.File;
import java.io.IOException;
import java.util.Date;

final class Application {
    private static final String DEFAULT_PATH = "." + File.separator + "log";
    private static String[] args;

    Application(String[] args) {
        Application.args = args;
    }

    void start() {
        Pair<Date[], long[]> result;
        try {
            result = new Reader().getUsedMemory(getPath());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        long totalMemory = result.getValue()[result.getValue().length - 1];
        System.out.println("Total memory: " + totalMemory);
        for (int i = 0; i < result.getKey().length; i++) {
            System.out.println(result.getKey()[i] + " : " + result.getValue()[i]);
        }
        Drawer drawer = new Drawer(result.getKey(), result.getValue());
        drawer.setVisible(true);
    }

    private String getPath() {
        return args.length != 0 ? args[0] : DEFAULT_PATH;
    }
}
