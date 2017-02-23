package com.nfa_dfa;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Parser p = new Parser(args[1]);

        List<String[]> StateToTest = new ArrayList<>();
        List<Transition> transition = new ArrayList<>();

        StateToTest.add(new String[]{p.StartingState()});

        while (StateToTest.size() > 0)
        {
            String[] test = StateToTest.get(0);
            for(int x = 0; x < test.length; x++) {

            }
        }

    }

    public static void Visit(Parser p,String state,String symbol,HashMap<String,Integer> visit)
    {
        if(visit.containsKey(state) )
        {
            Integer count = visit.get(symbol);
            visit.put(state, ++count);
            if(count == 2)
                return;
        }
        else
            visit.put(symbol, 1);


        Iterator<String> iter = p.GetTransitions(state, symbol);
        for (Iterator<String> it = iter; it.hasNext(); ) {
            String nextState = it.next();
            Main.Visit(p,nextState,symbol,visit);

        }

    }


}
