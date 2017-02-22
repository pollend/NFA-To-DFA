package com.nfa_dfa;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by michaelpollind on 2/21/17.
 */
enum  TransitionType{
    NORMAL,
    EPSILON
}

public class Parser {


    private String startingState;
    private String[] possibleStates;
    private String[] acceptedStates;
    private String[] epsilonClosure;
    private Map<String, Map<String,List<String>>> transition = new HashMap<>();

    public  Parser(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner scanner = new Scanner(file);

        if(!scanner.hasNext()) throw new RuntimeException("illegal file");
        possibleStates = scanner.nextLine().trim().split("\t");

        if(!scanner.hasNext()) throw new RuntimeException("illegal file");
        epsilonClosure = scanner.nextLine().trim().split("\t");

        if(!scanner.hasNext()) throw new RuntimeException("illegal file");
        startingState =  scanner.nextLine().trim();

        if(!scanner.hasNext()) throw new RuntimeException("illegal file");
        acceptedStates = scanner.nextLine().trim().split("\t");

        Pattern p = Pattern.compile("([a-zA-Z]+),([a-zA-Z]+)=([a-zA-Z]+)");
        while (scanner.hasNext())
        {
            String line = scanner.nextLine().replaceAll("\\s","").trim();
            Matcher match = p.matcher(line);
            if(!match.hitEnd() && match.groupCount() != 3)
                throw new RuntimeException("unknown transition: " + line);
            String currentState = match.group(0);
            String symbol = match.group(2);
            String next = match.group(3);

            if(!IsInEpsilon(symbol))
                throw new RuntimeException("symbol not in epsilon: " + symbol);

            if(!IsPossibleState(currentState))
                throw new RuntimeException("state not possible: " + currentState);

            if(!IsPossibleState(next))
                throw new RuntimeException("state not possible: " + next);

            Map<String, List<String>> lookup = transition.putIfAbsent(currentState, new HashMap<>());
            List<String> tr =  lookup.putIfAbsent(symbol,new ArrayList<>());
            tr.add(next);

        }
    }

    public boolean IsInEpsilon(String c)
    {
        for (String ep : epsilonClosure) {
            if (ep.equals(c))
                return true;
        }
        return  false;
    }

    public  boolean IsPossibleState(String c)
    {
        for (String ep : possibleStates) {
            if (ep.equals(c))
                return true;
        }
        return  false;
    }
    public  boolean IsPossibleAcceptState(String c)
    {
        for (String ep : acceptedStates) {
            if (ep.equals(c))
                return true;
        }
        return  false;
    }

    public Iterator<String> GetTransitions(String state,String symbol)
    {
        if(!transition.get(state).containsKey(symbol))
            return  null;

        return transition.get(state).get(symbol).iterator();
    }

    public  String StartingState()
    {
        return  startingState;
    }




}
