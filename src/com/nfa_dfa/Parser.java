

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
    private String[] dfaState = new String[10];
    private HashMap<String, HashMap<String,List<String>>> transition = new HashMap<>();
    
    public  Parser(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        
        if(!scanner.hasNext()) throw new RuntimeException("illegal file");
        possibleStates = scanner.nextLine().trim().split("\\s+");
        
        if(!scanner.hasNext()) throw new RuntimeException("illegal file");
        epsilonClosure = scanner.nextLine().trim().split("\\s+");
        
        if(!scanner.hasNext()) throw new RuntimeException("illegal file");
        startingState =  scanner.nextLine().trim();
        
        if(!scanner.hasNext()) throw new RuntimeException("illegal file");
        acceptedStates = scanner.nextLine().trim().split("\\s+");
        
        Pattern p = Pattern.compile("([a-zA-Z0-9{,}]+),([a-zA-Z0-9]+)=([a-zA-Z0-9{,}]+)");
        
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
            String currentState = match.group(1);
            String symbol = match.group(2);
            String next = match.group(3);
            
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
    
    public String[] DFAState(){
        return dfaState;
    }
    
    
}
