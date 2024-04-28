package auxil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HMM {

    private final List<Character> alphabet;
    private final List<Character> states;
    private final Map<Character, Map<Character, Double>> transition;
    private final Map<Character, Map<Character, Double>> emission;

    public HMM(
            List<Character> alphabet, List<Character> states,
            Map<Character, Map<Character, Double>> transition,
            Map<Character, Map<Character, Double>> emission
    ) {
        this.alphabet = Collections.unmodifiableList(alphabet);
        this.states = Collections.unmodifiableList(states);
        this.transition = transition;
        this.emission = emission;
    }

    public List<Character> getAlphabet() {
        return alphabet;
    }

    public int alphabetSize() {
        return alphabet.size();
    }

    public char getNthSymbol(int n) {
        return alphabet.get(n);
    }

    public List<Character> getStates() {
        return states;
    }

    public int numStates() {
        return states.size();
    }

    public char getNthState(int n) {
        return states.get(n);
    }

    public double getTransitionProbability(char fromState, char toState) {
        return transition.get(fromState).get(toState);
    }

    public double getEmissionProbability(char state, char symbol) {
        return emission.get(state).get(symbol);
    }
}
