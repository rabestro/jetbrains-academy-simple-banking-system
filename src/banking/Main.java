package banking;

import banking.menu.Menu;
import banking.repository.InMemoryDatabase;
import banking.repository.SQLiteDatabase;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public final class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());

    static {
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream("logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }
    }

    public static void main(String[] args) {
        log.info("Simple Banking System started.");

        final var app = new Application(args.length == 2 && "-fileName".equals(args[0])
                ? new SQLiteDatabase(args[1])
                : new InMemoryDatabase(new ArrayList<>()));

        new Menu("Simple Banking System")
                .add("Create an account", app::createAccount)
                .add("Log into account", app::logIntoAccount)
                .run();
    }
}
