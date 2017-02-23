package com.nfa_dfa;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    public static String[] TestSpace(String state,String symbol,List<String> visit)
    {
        return  new String[]{};
    }

}
