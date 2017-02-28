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

        Queue<String[]> StateToTest = new LinkedList<>();

        HashSet<String[]> state = new HashSet<>();
        HashSet<String> transitions = new HashSet<>(); 


        //add starting state to list
        StateToTest.add(new String[]{p.StartingState()});
         //do conversion
        while (StateToTest.size() > 0) {
            String[] test = StateToTest.remove();

            for (String c : p.EpsilonClosure()) {
                HashSet<String> visits = new HashSet<>();
                for (int x = 0; x < test.length; x++) {
                    Main.Visit(p, test[x], c, visits, true);

                }
                String key = ConvertToHashKey(Arrays.stream(test), c, visits.stream());
                if (!transitions.contains(key)) {
                    transitions.add(key);
                    StateToTest.add(visits.toArray(new String[visits.size()]));
                }
                state.add(test);
                state.add(visits.toArray(new String[visits.size()]));


            }
        }


        //create file 
        File f2  = new File("test.dfa");
        try {
            f2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter writer = new FileWriter(f2);


            writer.write(state.stream().map(j -> GroupTokens(Arrays.stream(j))).distinct().collect(Collectors.joining("\t")) + "\n");     //write states
            writer.write(Arrays.stream(p.EpsilonClosure()).collect(Collectors.joining("\t")) + "\n");  //write epsilon closures 
            writer.write("{"+p.StartingState() + "}\n"); //write start state
            writer.write(state.stream().filter(j -> Arrays.stream(j).anyMatch(r -> Arrays.asList(p.AcceptedStates()).contains(r))).map(x -> GroupTokens(Arrays.stream(x))).distinct().collect(Collectors.joining("\t")) + "\n");  //write Accept States

            for (String transition:transitions) {   //write transitions
                writer.write(transition + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    //this method finds duplicates and then adds it to result
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
        return result;
    }


    public static String GroupTokens(Stream<String> str) {
        return "{" + str.collect(Collectors.joining(", ")) + "}";
    }

    public static String ConvertToHashKey(Stream<String> states, String key, Stream<String> to) {
        return GroupTokens(states) + "," + key + "=" + GroupTokens(to);
    }
    //keep track of what is visited 
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
