package com.navigamez.greex;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;

import java.util.*;
import java.util.concurrent.TimeUnit;

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
     */
    public String generateRandom() {
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
     */
    public String generateRandom(long seed) {
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
     */
    public String generateRandom(Random random) {
        return GreexRandomGenerator.generateRandom(this.automaton, random);
    }

    /**
     * Generates multiple, unique random matches for this generator's regular expression. This uses
     * the given {@link Random} instance and defaults to no timeout.
     * <p>
     * This method is only thread safe if the given {@link Random} is managed in a thread-safe way.
     *
     * @param random the {@link Random} to use for generation.
     * @param count the number of matches to generate
     * @return a random string that matches the given regular expression
     */
    public List<String> generateRandom(Random random, int count) throws StackOverflowError {
        return generateRandom(random, count, true);
    }

    /**
     * Generates multiple, unique random matches for this generator's regular expression. This
     * creates a new {@link Random} instance using the given {@code seed} and defaults to no
     * timeout. Subsequent calls with the same seed will create a new {@link Random} each time, and
     * so this will always return the same result for the same regular expression and seed.
     * <p>
     * This method is always thread safe.
     *
     * @param seed the seed to use for the {@link Random} instance.
     * @param count the number of matches to generate
     * @return a random string that matches the given regular expression
     */
    public List<String> generateRandom(long seed, int count) throws StackOverflowError {
        return generateRandom(new Random(seed), count);
    }

    /**
     * Generates multiple random matches for this generator's regular expression. This uses the
     * given {@link Random} instance and defaults to no timeout.
     * <p>
     * Because of the random nature of this method, it's possible that when {@code unique} is true,
     * calculating random matches may result in throwing away several generated matches and this
     * method could take a long time. To avoid this issue, use
     * {@link #generateRandom(Random, int, boolean, long, TimeUnit)} instead.
     * <p>
     * This method is only thread safe if the given {@link Random} is managed in a thread-safe way.
     *
     * @param random the {@link Random} to use for generation.
     * @param count the number of matches to generate
     * @param unique {@code true} if the matches must be unique
     * @return a random string that matches the given regular expression
     */
    public List<String> generateRandom(Random random, int count, boolean unique) throws StackOverflowError {
        return generateRandom(random, count, unique, -1, null);
    }

    /**
     * Generates multiple random matches for this generator's regular expression. This creates a new
     * {@link Random} instance using the given {@code seed} and defaults to no timeout. Subsequent
     * calls with the same seed will create a new {@link Random} each time, and so this will always
     * return the same result for the same regular expression and seed.
     * <p>
     * Because of the random nature of this method, it's possible that when {@code unique} is true,
     * calculating random matches may result in throwing away several generated matches and this
     * method could take a long time. To avoid this issue, use
     * {@link #generateRandom(Random, int, boolean, long, TimeUnit)} instead.
     * <p>
     * This method is always thread safe.
     *
     * @param seed the seed to use for the {@link Random} instance.
     * @param count the number of matches to generate
     * @param unique {@code true} if the matches must be unique
     * @return a random string that matches the given regular expression
     */
    public List<String> generateRandom(long seed, int count, boolean unique) throws StackOverflowError {
        return generateRandom(new Random(seed), count, unique, -1, null);
    }

    /**
     * Generates multiple random matches for this generator's regular expression. This uses the
     * given {@link Random} instance.
     * <p>
     * Because of the random nature of this method, it's possible that when {@code unique} is true,
     * calculating random matches may result in throwing away several generated matches and this
     * method could take a long time. This variant has a timeout option that can be specified that
     * will make this method return if the timeout is exceeded. Note that this will not fail-fast on
     * the timeout. If the code is generating a match at the time that the timeout is exceeded, then
     * this method won't return until that final match is generated. To disable timeouts, either
     * pass a non-positive number for {@code count} ({@code count <= 0}) or {@code null} for
     * {@code timeoutUnit}.
     * <p>
     * This method is only thread safe if the given {@link Random} is managed in a thread-safe way.
     *
     * @param random the {@link Random} to use for generation.
     * @param count the number of matches to generate
     * @param unique {@code true} if the matches must be unique
     * @param timeout the number of {@link TimeUnit}s to wait before returning, or a number &lt;= 0
     *                to disable timeouts
     * @param timeoutUnit the {@link TimeUnit} for the timeout, or {@code null} to disable timeouts
     * @return a random string that matches the given regular expression
     */
    public List<String> generateRandom(Random random, int count, boolean unique, long timeout, TimeUnit timeoutUnit) throws StackOverflowError {
        Collection<String> results = unique ? new HashSet<String>(count) : new ArrayList<String>(count);
        long stop;
        if (timeout <= 0 || timeoutUnit == null) {
            stop = -1;
        } else {
            stop = timeoutUnit.toMillis(timeout) + System.currentTimeMillis();
        }
        while (results.size() < count) {
            long now = System.currentTimeMillis();
            if (stop > 0 && stop < now) {
                break;
            }
            results.add(generateRandom(random));
        }
        return new ArrayList<String>(results);
    }

    /**
     * Generates multiple random matches for this generator's regular expression. This creates a new
     * {@link Random} instance using the given {@code seed} and defaults to no timeout. Subsequent
     * calls with the same seed will create a new {@link Random} each time, and so this will always
     * return the same result for the same regular expression and seed.
     * <p>
     * Because of the random nature of this method, it's possible that when {@code unique} is true,
     * calculating random matches may result in throwing away several generated matches and this
     * method could take a long time. This variant has a timeout option that can be specified that
     * will make this method return if the timeout is exceeded. Note that this will not fail-fast on
     * the timeout. If the code is generating a match at the time that the timeout is exceeded, then
     * this method won't return until that final match is generated. To disable timeouts, either
     * pass a non-positive number for {@code count} ({@code count <= 0}) or {@code null} for
     * {@code timeoutUnit}.
     * <p>
     * This method is always thread safe.
     *
     * @param seed the seed to use for the {@link Random} instance.
     * @param count the number of matches to generate
     * @param unique {@code true} if the matches must be unique
     * @param timeout the number of {@link TimeUnit}s to wait before returning, or a number &lt;= 0
     *                to disable timeouts
     * @param timeoutUnit the {@link TimeUnit} for the timeout, or {@code null} to disable timeouts
     * @return a random string that matches the given regular expression
     */
    public List<String> generateRandom(long seed, int count, boolean unique, long timeout, TimeUnit timeoutUnit) throws StackOverflowError {
        return generateRandom(new Random(seed), count, unique, timeout, timeoutUnit);
    }
}
