package com.navigamez.greex;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;

import java.util.Random;
import java.util.Set;

/**
 * Generates matches for a given regular expression. Support for regular expressions is provided by
 * {@link RegExp}.
 * <p>
 * To use this class, create a new generator instance with your regular expression, then use the
 * generate methods to generate your matches. This example will use this regular expression:
 * <p>
 * <code>
 * (white|black)|((light|dark) )?(red|green|blue|gray)
 * </code>
 * <p>
 * To generate a random match, create an instance of {@link GreexGenerator} and use
 * {@link #generateRandom}:
 * <p>
 * <code>
 * GreexGenerator generator = new GreexGenerator("(white|black)|((light|dark) )?(red|green|blue|gray)");<br>
 * String match = generator.generateRandom();<br>
 * System.out.println(match); // e.g. "dark red"
 * </code>
 * <p>
 * To generate all matches, create an instance of {@link GreexGenerator} and use
 * {@link #generateAll}:
 * <p>
 * <code>
 * GreexGenerator generator = new GreexGenerator("(white|black)|((light|dark) )?(red|green|blue|gray)");<br>
 * List&lt;String&gt; matches = generator.generateAll();<br>
 * System.out.println(matches.size()); // "14"
 * </code>
 * <p>
 * When generating all matches for a non-finite regular expression, you must provide a max length or
 * you will get a {@link StackOverflowError} or {@link OutOfMemoryError}. The max length defaults to
 * {@link Integer#MAX_VALUE} when not provided, which is fine for finite regular expressions.
 * <p>
 * <b>Thread Safety</b>
 * <p>
 * This class is not 100% thread safe. Here is the thread safety profile of each method.
 * <table>
 * <tr>
 * <td><b>Method</b></td>
 * <td><b>Thread Safety</b></td>
 * </tr>
 * <tr>
 * <td>{@link #generateAll()}</td>
 * <td>Always thread safe</td>
 * </tr>
 * <tr>
 * <td>{@link #generateAll(int)}</td>
 * <td>Always thread safe</td>
 * </tr>
 * <tr>
 * <td>{@link #generateRandom()}</td>
 * <td><font color="red">Not thread safe*</font></td>
 * </tr>
 * <tr>
 * <td>{@link #generateRandom(long)}</td>
 * <td>Always thread safe</td>
 * </tr>
 * <tr>
 * <td>{@link #generateRandom(Random)}</td>
 * <td><i>Sometimes</i> thread safe**</td>
 * </tr>
 * </table>
 * <p>
 * * This method uses an internal {@link Random} with no synchronization, and so it is not thread
 * safe.<br>
 * ** This method is only thread safe if the calling class is managing the {@link Random} in a
 * thread-safe way.
 *
 * @author Brian Saltz
 * @since 1.0
 */
public class GreexGenerator {

    private final Automaton automaton;
    private final Random random = new Random();

    /**
     * Create a new generator using the given regular expression.
     *
     * @param regex the regular expression that will be used for match generation
     */
    public GreexGenerator(String regex) {
        if (regex == null) {
            throw new IllegalArgumentException("regex cannot be null");
        }
        this.automaton = new RegExp(regex).toAutomaton();
    }

    /**
     * Generate all the matches for this generator's regular expression. This method is the same as
     * invoking {@code generateAll(Integer.MAX_VALUE)}.
     * <p>
     * This method is always thread safe.
     *
     * @return an unordered set of all matches
     * @throws OutOfMemoryError   might be thrown if the regular expression is non-finite
     * @throws StackOverflowError might be thrown if the regular expression is non-finite
     */
    public Set<String> generateAll() throws StackOverflowError, OutOfMemoryError {
        return generateAll(Integer.MAX_VALUE);
    }

    /**
     * Generate all the matches for this generator's regular expression where the length of the
     * generated string is less than the given maximum length.
     * <p>
     * This method is always thread safe.
     *
     * @param maxLength the maximum string length for generated matches
     * @return an unordered set of all matches with lengths less than the given maximum length
     * @throws OutOfMemoryError   might be thrown if the regular expression is non-finite
     * @throws StackOverflowError might be thrown if the regular expression is non-finite
     */
    public Set<String> generateAll(int maxLength) throws StackOverflowError, OutOfMemoryError {
        return GreexAllGenerator.generateAll(automaton, maxLength);
    }

    /**
     * Generates a random match for this generator's regular expression. This uses an internal
     * {@link Random} that was created when the instance was constructed. Subsequent calls continue
     * to use the same {@link Random} instance.
     * <p>
     * This method is never thread safe.
     *
     * @return a random string that matches the given regular expression
     * @throws StackOverflowError might be thrown if the regular expression is non-finite
     */
    public String generateRandom() throws StackOverflowError {
        return generateRandom(random);
    }

    /**
     * Generates a random match for this generator's regular expression. This creates a new
     * {@link Random} instance using the given {@code seed}. Subsequent calls with the same seed
     * will create a new {@link Random} each time, and so this will always return the same result
     * for the same regular expression and seed.
     * <p>
     * This method is always thread safe.
     *
     * @param seed the seed to use for the {@link Random} instance.
     * @return a random string that matches the given regular expression
     * @throws StackOverflowError might be thrown if the regular expression is non-finite
     */
    public String generateRandom(long seed) throws StackOverflowError {
        return generateRandom(new Random(seed));
    }

    /**
     * Generates a random match for this generator's regular expression. This uses the given
     * {@link Random} instance.
     * <p>
     * This method is only thread safe if the given {@link Random} is managed in a thread-safe way.
     *
     * @param random the {@link Random} to use for generation.
     * @return a random string that matches the given regular expression
     * @throws StackOverflowError might be thrown if the regular expression is non-finite
     */
    public String generateRandom(Random random) throws StackOverflowError {
        return GreexRandomGenerator.generateRandom(this.automaton, random);
    }
}
