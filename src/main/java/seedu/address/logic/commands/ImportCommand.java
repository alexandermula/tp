package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_IMPORT_ERROR;

import java.nio.file.Path;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.storage.JsonAddressBookStorage;

/**
 * Imports data from the JSON or CSV file located at the specified path into FinBook.
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Imports data from the JSON or CSV file located at the specified path.\n"
            + "Parameters: path to JSON or CSV file\n"
            + "Example: " + COMMAND_WORD + " ./somewhere/addressbook.json";

    public static final String MESSAGE_IMPORT_DATA_SUCCESS = "Imported data: %1$s";

    private final Path targetPath;

    public ImportCommand(Path targetPath) {
        this.targetPath = targetPath;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (targetPath.toString().toLowerCase().endsWith(".json")) {
            JsonAddressBookStorage tempStorage = new JsonAddressBookStorage(targetPath);
            try {
                tempStorage.readAddressBook().ifPresent(x -> x.getPersonList().forEach(y -> model.addPerson(y)));
            } catch (Exception e) {
                throw new CommandException(String.format(MESSAGE_IMPORT_ERROR, e.getMessage()));
            }
        }
        return new CommandResult(String.format(MESSAGE_IMPORT_DATA_SUCCESS, targetPath.getFileName()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ImportCommand // instanceof handles nulls
                && targetPath.equals(((ImportCommand) other).targetPath)); // state check
    }
}
