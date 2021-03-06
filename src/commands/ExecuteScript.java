package commands;

import managers.CollectionManager;
import managers.IOManager;
import managers.CommandManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static utils.CommandsListAggregator.createCommandsList;

/**
 * Класс, описывающий конкретную реализацию команды execute script.
 * <p>
 * Команда аргументом принимает полный абсолютный путь к файлу.
 *
 * @author Базанов Евгений.
 */
public class ExecuteScript extends Command {
    public ExecuteScript(CollectionManager manager, IOManager ioManager) {
        super("execute_script",
                "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь",
                "file_name",
                1,
                manager,
                ioManager);
    }

    @Override
    public void execute(String... args) {
        final String arg = args[0];
        final Path path = Paths.get(arg);
        if (!Files.exists(path) || !Files.isReadable(path)) {
            System.err.println("Файл не существует или недоступен для чтения");
            return;
        }
        try {
            PrintStream outputToMemory = new PrintStream(new ByteArrayOutputStream());
            IOManager ioManager = new IOManager(outputToMemory, System.out, new Scanner(path));
            CommandManager commandManager = new CommandManager(manager, ioManager, createCommandsList(manager, ioManager));
            commandManager.executeScript();
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла");
            return;
        }

    }
}