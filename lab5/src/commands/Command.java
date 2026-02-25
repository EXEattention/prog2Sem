package commands;

/**
 * @author Кияшко.A.М 505196 P3118
 */
public interface Command {
    void execute(String... args);

    String getName();

    String description();
}