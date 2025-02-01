package mana.command;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mana.command.Command.EMPTY_KEYWORD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandTest {
    @Test
    public void action_noOp_success() {
        Command<?> c = new Command<>("test").withAction((t, m) -> Command.CommandResult.OK);
        assertEquals(Command.CommandResult.OK, c.execute(null, null));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void action_identity_success() {
        Command<List<String>> c = new Command<List<String>>("test")
                .withParameterTransform(EMPTY_KEYWORD, l -> l)
                .withAction((l, args) -> {
                    assertTrue(l.addAll((Collection<? extends String>) args.get(EMPTY_KEYWORD)));
                    return Command.CommandResult.OK;
                });
        List<String> target = new ArrayList<>();
        List<String> source = List.of(new String[]{"a", "b", "c"});
        Map<String, List<String>> args = new HashMap<>();
        args.put(EMPTY_KEYWORD, source);
        
        assertEquals(Command.CommandResult.OK, c.execute(target, args));
        assertEquals(target, source);
    }

    @Test
    public void action_null_exceptionThrown() {
        Command<?> c = new Command<>("test");
        assertThrows(NullPointerException.class, () -> c.execute(null, null));
    }
}
