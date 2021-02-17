package duke.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

/**
 * Parser ensures that only valid inputs are accepted.
 */
public class Parser {

    /**
     * Checks if input from user is valid and returns a pair of command and arugments.
     * <br>Only accepts valid commands, followed by valid parameters.
     *
     * @param input Input from user.
     * @return Pair of command and the arguments.
     * @throws DukeInputException If input is invalid.
     */
    public static Pair<Command, String> parseInput(String input) throws DukeInputException {
        String[] s = input.split(" ", 2);

        Command command = getCommand(s[0]);
        String args = s.length == 2 ? s[1] : "";

        checkValidArguments(command, args);

        return new Pair<>(command, args);
    }
        try {
            cmd = Command.valueOf(s[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new DukeInputException("I'm sorry, but I don't know what that means :-(");
        }

        if (args.equals("-h")) {
            return;
        }

        switch(cmd) {
        case BYE:
            checkNoArgument(args);
            break;
        case CLEAR:
            checkNoArgument(args);
            break;
        case DEADLINE:
            checkValidDeadline(args);
            break;
        case DELETE:
            checkValidDoneDelete(args);
            break;
        case DONE:
            checkValidDoneDelete(args);
            break;
        case EVENT:
            checkValidEvent(args);
            break;
        case HELP:
            checkNoArgument(args);
            break;
        case HIGHPRIORITY:
            checkValidNumber(args);
            break;
        case LIST:
            checkNoArgument(args);
            break;
        case LOAD:
            checkNoArgument(args);
            break;
        case LOWPRIORITY:
            checkValidNumber(args);
            break;
        case SAMPLE:
            checkNoArgument(args);
            break;
        case SAVE:
            checkNoArgument(args);
            break;
        case SEARCH:
            checkValidSearch(args);
            break;
        case SORT:
            checkNoArgument(args);
            break;
        case TODO:
            checkValidTodo(args);
            break;
        default:
            break;
        }
    }

    /**
     * Checks if input from text file is valid.
     *
     * @param input Line from saved file.
     * @throws DukeInputException If input is invalid.
     */
    public static void checkImportFormat(String input) throws DukeInputException {
        String[] s = input.split(";", 4);

        if (s.length != 4) {
            throw new DukeInputException("Wrong number of arguments");
        }
        if (!(s[1].equals("0") || s[1].equals("1"))) {
            throw new DukeInputException("isDone column should be 0 or 1");
        }
        if (!(s[2].equals("0") || s[2].equals("1"))) {
            throw new DukeInputException("Priority column should be 0 or 1");
        }

        String[] args = s[3].split(";", 2);

        switch (s[0]) {
        case "T":
            checkValidTodo(args[0]);
            break;
        case "D":
            checkValidDeadline(String.join(" /by ", args));
            break;
        case "E":
            checkValidEvent(String.join(" /at ", args));
            break;
        default:
            throw new DukeInputException("Not a valid Task type");
        }
    }

    /**
     * Checks if the string in a valid LocalDate format (YYYY-MM-DD).
     *
     * @param s Input string.
     * @throws DukeInputException If not a valid LocalDate format.
     */
    private static void checkValidDate(String s) throws DukeInputException {
        try {
            LocalDate.parse(s);
        } catch (DateTimeParseException e) {
            throw new DukeInputException(
                String.format("\"%s\" is a wrong date format! Please use YYYY-MM-DD format.", s));
        }
    }

    private static void checkNoArgument(String s) throws DukeInputException {
        if (!s.isEmpty()) {
            throw new DukeInputException(String.format("%s is not a valid argument!", s));
        }
    }

    private static void checkValidTodo(String s) throws DukeInputException {
        if (s.length() == 0) {
            throw new DukeInputException("Please include a description of the Todo!");
        }
    }

    private static void checkValidDeadline(String s) throws DukeInputException {
        String[] args = s.split(" /by ");

        if (args.length < 2) {
            throw new DukeInputException("Description and duedate should be separated by \"/by\"");
        } else if (args.length > 2) {
            throw new DukeInputException("Please do not use \"/by\" multiple times!");
        }

        checkValidDate(args[1]);
    }

    private static void checkValidEvent(String s) throws DukeInputException {
        String[] args = s.split(" /at ");

        if (args.length < 2) {
            throw new DukeInputException("Description and date should separated by \"/at\"");
        } else if (args.length > 2) {
            throw new DukeInputException("Please do not use \"/at\" multiple times!");
        }

        checkValidDate(args[1]);
    }

    private static void checkValidDoneDelete(String input) throws DukeInputException {
        if (input.length() == 0) {
            throw new DukeInputException("Please input a task number!");
        }

        String[] args = input.split(" ");

        for (String s : args) {
            if (s.isEmpty()) {
                throw new DukeInputException("Please only leave one space between numbers!");
            }
            checkValidNumber(s);
        }

        Arrays.sort(args);
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals(args[i - 1])) {
                throw new DukeInputException("Please do not input duplicate numbers!");
            }
        }
    }

    private static void checkValidSearch(String s) throws DukeInputException {
        if (s.length() == 0) {
            throw new DukeInputException("Enter a keyword to search!");
        }
    }

    private static void checkValidNumber(String s) throws DukeInputException {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new DukeInputException(
                    String.format("\"%s\" is not a valid number!", s));
        }
    }

    /**
     * Checks if input is either a "y" (yes) or "n" (no).
     *
     * @param s Input from user.
     * @throws DukeInputException If input is neither a "y" or "n".
     */
    public static void parseYesNo(String s) throws DukeInputException {
        if (!(s.equals("y") || s.equals("n"))) {
            throw new DukeInputException("Invalid input! Please key in either \"y\" or \"n\"");
        }
    }
}
