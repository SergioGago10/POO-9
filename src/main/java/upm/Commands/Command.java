package upm.Commands;

public abstract class Command {
    String text;

    public Command(String text) {
        this.text = text;
    }

    public abstract boolean apply(String[] args);

    public String getText() {
        return text;
    }
}
