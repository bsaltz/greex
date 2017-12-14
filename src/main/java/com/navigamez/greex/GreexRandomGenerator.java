package com.navigamez.greex;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

import java.util.List;
import java.util.Random;

/**
 * <p>
 * This class was adapted from the <a href="https://github.com/bluezio/xeger">Xeger library</a>.
 * <p>
 * Using the {@link Automaton} class, this class generates random matches for the state machine.
 * This class can throw a {@link StackOverflowError} when generating a match for a non-finite
 * regular expression.
 */
class GreexRandomGenerator {

    static String generateRandom(Automaton automaton, Random random) throws StackOverflowError {
        StringBuilder builder = new StringBuilder();
        State state = automaton.getInitialState();
        do {
            List<Transition> transitions = state.getSortedTransitions(false);
            if (transitions.size() == 0) {
                break;
            }
            int maxOptions = state.isAccept() ? transitions.size() : transitions.size() - 1;
            int option = nextIntInclusive(0, maxOptions, random);
            if (state.isAccept() && option == 0) {
                // 0 is considered "stop"
                break;
            }
            // Moving on to next transition
            Transition transition = transitions.get(option - (state.isAccept() ? 1 : 0));
            append(builder, transition, random);
            state = transition.getDest();
        } while (true);
        return builder.toString();
    }

    private static void append(StringBuilder builder, Transition transition, Random random) {
        char c = (char) nextIntInclusive(transition.getMin(), transition.getMax(), random);
        builder.append(c);
    }

    private static int nextIntInclusive(int min, int max, Random random) {
        return random.nextInt(max - min + 1) + min;
    }
}
