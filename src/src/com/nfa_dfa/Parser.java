package src.com.nfa_dfa;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    
    
    private String startingState;
    private String[] possibleStates;
    private String[] acceptedStates;
    private String[] epsilonClosure;
    private HashMap<String, HashMap<String,List<String>>> transition = new HashMap<>();

    private String[]  GetEntries(String row){
        Pattern lookup = Pattern.compile("\\{(?:[a-zA-Z]+\\s*,*\\s*)*\\}|[a-zAz]+");
        Matcher matches = lookup.matcher(row);
        List<String> entries = new ArrayList<>();
        while(matches.find())
        {
            entries.add(matches.group().trim().replaceAll("\\s",""));
        }
        return entries.toArray(new String[entries.size()]);
    }

    public  Parser(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner scanner = new Scanner(file);

        if(!scanner.hasNext()) throw new RuntimeException("illegal file");
        possibleStates = GetEntries(scanner.nextLine());//scanner.nextLine().trim().split("\\s+");
        
        if(!scanner.hasNext()) throw new RuntimeException("illegal file");
        epsilonClosure = GetEntries(scanner.nextLine());//scanner.nextLine().trim().split("\\s+");
        
        if(!scanner.hasNext()) throw new RuntimeException("illegal file");
        startingState =  scanner.nextLine().replaceAll("\\s","");
        
        if(!scanner.hasNext()) throw new RuntimeException("illegal file");
        acceptedStates = GetEntries(scanner.nextLine());
        
        Pattern p = Pattern.compile("([a-zA-Z0-9{,} ]+),([a-zA-Z0-9]+)=([a-zA-Z0-9{,} ]+)");
        
        for (String state : possibleStates) {
            transition.put(state,new HashMap<String, List<String>>());
        }
        while (scanner.hasNext())
        {
            String line = scanner.nextLine().replaceAll("\\s","").trim();
            System.out.println(line);
            
            Matcher match = p.matcher(line);
            match.find();
            if(!match.hitEnd() && match.groupCount() != 3)
                throw new RuntimeException("unknown transition: " + line);
            String currentState = match.group(1).replaceAll("\\s","");
            String symbol = match.group(2);
            String next = match.group(3).replaceAll("\\s","");
            
            Map<String,List<String>> lookup = transition.get(currentState);
            if(lookup == null) {
                transition.put(currentState, new HashMap<>());
                lookup = transition.get(currentState);
            }
            
            List<String> tr = null;
            if(!lookup.containsKey(symbol)) {
                lookup.put(symbol, new ArrayList<String>());
                tr = lookup.get(symbol);
            }
            else tr = lookup.get(symbol);
            
            tr.add(next);
            
        }
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
    
    public  String[] PossibleStates(){
        return possibleStates;
    }
    
    public  String[] AcceptedStates(){
        return acceptedStates;
    }
    
    public String[] EpsilonClosure(){
        return  epsilonClosure;
    }

    
}
