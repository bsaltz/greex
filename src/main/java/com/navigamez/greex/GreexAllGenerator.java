package com.navigamez.greex;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

import java.util.*;

class GreexAllGenerator {

    static Set<String> generateAll(Automaton automaton, int maxLength, int maxCount) {
        Set<String> strings = new HashSet<String>();
        generateAll(automaton.getInitialState(), strings, new ArrayList<Character>(), maxLength, maxCount);
        return strings;
    }

    private static void generateAll(State state,
                                    Set<String> strings,
                                    List<Character> currentChoices,
                                    int maxLength,
                                    int maxCount) {
        if (strings.size() >= maxCount) {
            return;
        }
        List<Transition> transitions = state.getSortedTransitions(false);
        if (state.isAccept()) {
            strings.add(build(currentChoices));
        }
        if (currentChoices.size() >= maxLength) {
            return;
        }
        for (Transition transition : transitions) {
            for (char c = transition.getMin(); c <= transition.getMax(); c++) {
                currentChoices.add(c);
                generateAll(transition.getDest(), strings, currentChoices, maxLength, maxCount);
                currentChoices.remove(currentChoices.size() - 1);
            }
        }
    }

    private static String build(List<Character> characters) {
        StringBuilder sb = new StringBuilder(characters.size());
        for (Character c : characters) {
            sb.append(c);
        }
        return sb.toString();
    }
}
