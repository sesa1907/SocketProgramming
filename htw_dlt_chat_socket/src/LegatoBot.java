import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Task {
    String name;
    String time;
    String owner;
    Boolean isDone = false;

    Task(String owner, String name, String time) {
        this.name = name;
        this.time = time;
        this.owner = owner;
    }
}

public class LegatoBot {
    private Boolean isEnabled = false;
    private ChatServer server;
    private List<Task> tasksList = new ArrayList<Task>();
//    private final String LEGATO_CHAT_NICKNAME = "LEGATO";

    public LegatoBot(ChatServer server) {
        this.server = server;
//        this.announceToUsers("Legato has been added to chat, to activate please type in: legato start");
    }

    private void announceToUsers(String message) {
        String text = "[Bot-LEGATO]: "+message;
        this.server.broadcast(text, null);
    }

    public void talkToBot(String input) {
        // Commands list
        String ENABLE_LEGATO = "legato start";
        String DISABLE_LEGATO = "legato stop";
        String STATUS_LEGATO = "legato status";
        String HELP_LEGATO = "legato";
        String ADD_TASK = "^legato add [A-Za-z0-9 ]+ at (0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";
        String QUERY_TASKS = "legato tasks";

        // Lower-cased input string
        final String lowerCasedInput = input.toLowerCase();
        final String[] splittedStrings = lowerCasedInput.split("]:");

        String command = "";
        String sender = "";
        if (splittedStrings.length > 1) {
            command = splittedStrings[1].trim();
            sender = splittedStrings[0].split("\\[")[1];
        }

        if (command.equals(ENABLE_LEGATO)) {
            this.enable();
        } else if (command.equals(DISABLE_LEGATO)) {
            this.disable();
        } else if (command.equals(STATUS_LEGATO)) {
            this.announceToUsers(this.isEnabled ? "Legato is enabled" : "Legato is disabled");
        } else if (command.matches(ADD_TASK)) {
            if (!this.isEnabled) {
                this.announceToUsers(String.format("Please run '%s' to enable legato", ENABLE_LEGATO));
                return;
            }

            final String[] breakdown = command.split(" at ");
            final String taskName = breakdown[0].split("legato add ")[1];
            final String taskTime = breakdown[1];

            Task newTask = new Task(sender, taskName, taskTime);
            tasksList.add(newTask);
            this.announceToUsers("Success added task to task list!");
        } else if (command.equals(QUERY_TASKS)) {
            if (!this.isEnabled) {
                this.announceToUsers(String.format("Please run '%s' to enable legato", ENABLE_LEGATO));
                return;
            }

            String outputString = "";

            int i = 1;
            for (Task task : this.tasksList) {
                String taskString = String.format("Task #%d: %s at %s by %s, ", i, task.name, task.time, task.owner);
                outputString += taskString;
                i+=1;
            }

            this.announceToUsers(outputString);
        } else if (command.equals(HELP_LEGATO)) {
            final String helpText =
                    "Meet Legato! Your smart chat assistance! These are basic instructions in using Legato: "+
                    "1. Starting Legato -> 'legato start' "+
                    "2. Stopping Legato -> 'legato stop' "+
                    "3. Check Legato's status -> 'legato status' "+
                    "4. Add task -> 'legato add <task_name> at <HH:mm>' "+
                    "5. List all tasks -> 'legato tasks' ";
            this.announceToUsers(helpText);
        } else if (command.startsWith("legato ")) {
            this.announceToUsers(String.format("Invalid legato command, try tun '%s' for help", HELP_LEGATO));
        }
    }

    private void enable() {
        if (!isEnabled) {
            this.isEnabled = true;
            this.announceToUsers("Hello, I'm Legato, a bot designed for accommodating your needs!");
        } else {
            this.announceToUsers("Legato is already enabled!");
        }
    }

    private void disable() {
        if (isEnabled) {
            this.isEnabled = false;
            this.announceToUsers("Legato will take a nap now...");
        } else {
            this.announceToUsers("Legato is already disabled!");
        }
    }
}
