package com.nfa_dfa;


import javax.swing.plaf.nimbus.State;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Parser p = new Parser("test.nfa");

        List<String[]> StateToTest = new ArrayList<>();

        HashSet<String[]> state = new HashSet<>();
        HashSet<String> transitions = new HashSet<>();


        StateToTest.add(new String[]{p.StartingState()});

        while (StateToTest.size() > 0) {
            String[] test = StateToTest.get(0);

            for (String c : p.EpsilonClosure()) {
                HashSet<String> visits = new HashSet<>();
                for (int x = 0; x < test.length; x++) {
                    Main.Visit(p, test[0], c, visits, true);
                }
                String key = ConvertToHashKey(Arrays.stream(test), c, visits.stream());
                if (!transitions.contains(key)) {
                    transitions.add(key);
                    StateToTest.add(visits.toArray(new String[visits.size()]));
                }
                state.add(test);
                state.add(visits.toArray(new String[visits.size()]));

            }
            StateToTest.remove(0);
        }



        File f2  = new File("test.dfa");
        try {
            f2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter writer = new FileWriter(f2);
           // writer.write(state.stream().collect(Collectors.joining("\t")) + "\n");
           // writer.write(Arrays.stream(p.EpsilonClosure()).collect(Collectors.joining("\t")) + "\n");

            for (String transition:transitions) {
                writer.write(transition + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public  static List<String[]> FindMatching(HashSet<String[]> states,String matchToken)
    {
        List<String[]> result = new ArrayList<>();
        for(String[] state : states)
        {
            for(String token : state)
            {
                if(token.equals(matchToken)) {
                    result.add(state);
                    break;
                }
            }

        }
    }


    public static String GroupTokens(Stream<String> str) {
        return "{" + str.collect(Collectors.joining(", ")) + "}";
    }

    public static String ConvertToHashKey(Stream<String> states, String key, Stream<String> to) {
        return GroupTokens(states) + "," + key + "=" + GroupTokens(to);
    }

    public static void Visit(Parser p, String state, String symbol, HashSet<String> visit, boolean start) {
        if (!start) {
            if (visit.contains(state))
                return;
            visit.add(state);
        }

        Iterator<String> iter2 = p.GetTransitions(state, "EPS");
        if (iter2 != null) {
            for (Iterator<String> it = iter2; it.hasNext(); ) {
                String nextState = it.next();
                Main.Visit(p, nextState, symbol, visit, false);
            }
        }


        Iterator<String> iter = p.GetTransitions(state, symbol);
        if (iter != null) {
            for (Iterator<String> it = iter; it.hasNext(); ) {
                String nextState = it.next();
                Main.Visit(p, nextState, symbol, visit, false);
            }
        }

    }


}
