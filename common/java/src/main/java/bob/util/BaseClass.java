package bob.util;

import bob.parser.PuzzleDataParser;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;

/**
 * Base class for all puzzle solution programs.
 * <p>
 * Classes that extend this class are constrained to have a name that starts with "{@code Day}" followed by a two-digit
 * day number. The day number is not parsed as a number but is used to select the proper puzzle input file.
 * </p>
 * <p>
 * The {@code main} method for each subclass is expected to have a single line like {@code new Day35a().run(args, "")}.
 * The subclass constructor is expected to have two lines like {@code super(false)} and {@code setParser(parser)}.
 * </p>
 * <p>
 * The {@code LOG} logger should be used with the following levels:
 * </p>
 * <ul>
 * <li><b>INFO</b> - A normal message</li>
 * <li><b>DEBUG</b> - A debug message</li>
 * </ul>
 */
@Getter
public abstract class BaseClass<T> {

    /**
     * An instance to parse the puzzle data.
     */
    @Setter(AccessLevel.PROTECTED)
    private PuzzleDataParser<T> parser;

    /**
     * Logger for displaying messages.
     */
    protected org.slf4j.Logger LOG;

    /**
     * {@code true} if the real input data is being used.
     */
    private boolean realData;

    public BaseClass(boolean debug) {
        // Set up logging
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
        encoder.setContext(lc);

        PatternLayout layout = new PatternLayout();
        layout.setPattern("%msg%n");
        layout.setContext(lc);
        layout.start();
        encoder.setLayout(layout);

        ConsoleAppender<ILoggingEvent> ca = new ConsoleAppender<>();
        ca.setContext(lc);
        ca.setName("console");
        ca.setEncoder(encoder);
        ca.start();

        Logger rootLogger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.detachAndStopAllAppenders();
        rootLogger.addAppender(ca);
        rootLogger.setLevel(debug ? Level.DEBUG : Level.INFO);

        LOG = LoggerFactory.getLogger(getClass());
    }

    /**
     * The method to actually solve the puzzle, called after the puzzle input has been parsed.
     */
    public abstract void solve(T data);

    /**
     * Run through the puzzle solution. This method is intended to be the only line in the subclass {@code main} method.
     * <p>
     * The first command line argument is the name of the directory holding the puzzle input data and, if specified,
     * should be either "{@literal real}" or "{@literal sample}". If not specified, "{@literal sample}" is used.
     * </p>
     *
     * @param args Command line arguments
     * @param inputSuffix Suffix to use for alternate sample data
     */
    public void run(String[] args, String inputSuffix) {

        // Check name of puzzle class
        String classname = getClass().getSimpleName();
        Assert.that(classname.startsWith("Day"), "Puzzle class name does not start with 'Day'");

        // Ensure the constructor defined a parser for the input
        Assert.that((parser != null), "data parser not set");

        // Read the puzzle input
        String datadir = (args.length > 0) ? args[0] : "sample";
        realData = datadir.equals("real");
        readPuzzleData(datadir, classname.substring(3, 5), (realData ? "" : inputSuffix));

        // Run the solution
        long start = System.currentTimeMillis();
        solve(parser.getResult());
        System.out.println("Run time: " + (System.currentTimeMillis() - start) + " ms");
    }

    // This is done as a separate method so it can be replaced in test classes
    BufferedReader getPuzzleDataReader(String datadir, String daynum, String inputSuffix) throws IOException {
        String filename = "../data/" + datadir + "/" + daynum + inputSuffix + ".txt";
        return new BufferedReader(new FileReader(filename));
    }

    // Read each line of the puzzle input and send it to the parser for processing
    private void readPuzzleData(String datadir, String daynum, String inputSuffix) {
        try {
            // Open the file
            BufferedReader rdr = getPuzzleDataReader(datadir, daynum, inputSuffix);

            // Start the first part
            int partnum = 0;
            parser.open(partnum);

            // Read lines until EOF
            String line = rdr.readLine();
            while (line != null) {
                // Read lines until blank line or EOF
                while ((line != null) && (!line.isBlank())) {
                    parser.read(partnum, line);
                    line = rdr.readLine();
                }
                // If blank line, switch to the next part
                if (line != null) {
                    parser.close(partnum);
                    partnum++;
                    parser.open(partnum);
                    line = rdr.readLine();
                }
            }

            // Close the last part
            parser.close(partnum);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
