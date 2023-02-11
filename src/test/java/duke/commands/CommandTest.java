package duke.commands;

import org.junit.jupiter.api.Test;

import duke.Duke;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommandTest {
    @Test
    public void checkValidation() {
        Command test = new Command("") {
            @Override
            public void executeInternal(String[] tokens, Duke instance) throws ValidationException {
                validate(false, "Error Message");
            } 
        };

        assertThrows(
            Command.ValidationException.class, 
            () -> test.executeInternal(null, null), 
            "validate() should throw Validation exception when called with a false boolean condition"
        );
    }
}
