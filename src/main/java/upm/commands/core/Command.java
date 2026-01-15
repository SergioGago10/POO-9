package upm.commands.core;


public abstract class Command {
    protected String text;

    public Command(String text) {
        this.text = text;
    }

    public abstract boolean apply(String[] args);

    public String getText() {
        return text;
    }
}
