package Triehard;

import Model.Sequence;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Kris
 */
public class SimpleTrie implements BasicTrie{
    private static StringBuilder outputString = new StringBuilder();
    private final TrieNode root;
    private int size;
    
    public SimpleTrie(){
        TrieNode.reset();
        outputString = new StringBuilder();
        root = new TrieNode();
        size =0;
    }
    
    @Override
    public void addSequence(Sequence s) {
        recursiveAdd(s.getBases(),root);
        size++;
    }
    
    public void addAll(Collection<Sequence> c){
        for(Sequence sequence: c){
            addSequence(sequence);
        }
    }
    
    @Override
    public boolean contains(Sequence s) {
        return found(s.getBases(),root);
    }

    @Override
    public void removeSequence(Sequence s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String rosalindOutput() {
        return outputString.toString();
    }
    
    /**
     * Used to generate rosland output. Output is created while trie is created to increase efficency.
     * @param text 
     */
    static void addOutputLine(String text){
        outputString.append(text);
    }

    /**
     * Does the heavy lifting on adding a new sequence to the trie.
     * @param bases String left to be added to the trie.
     * @param node Our current location in the trie.
     */
    private void recursiveAdd(String bases, TrieNode node) {
        //We should never add a sequence of nothing.
        assert (bases.length()>=1);
        
        char currentSymbol = bases.charAt(0);
        TrieNode child = node.addChild(currentSymbol);
        
        if(bases.length()>1){
            recursiveAdd(bases.substring(1),child);
        }
        
    }

    @Override
    public int size() {
        return size;
    }
    
    /**
     * Searches for all the possible matches in the pattern. Probably not terribly useful for my merge
     * algorithm, but it's possible someone might be able to use this code for something else.
     * Such as if we decide to store the whole genome this way.
     * @param text - The text we are searching for matches in the trie.
     * @return The list of the beginning node indexes for all possible matches.
     */
    public List<Integer> walk(Sequence text){
        ArrayList<Integer> output = new ArrayList<>();
        return explore(output,text.getBases());
    }
    /**
     * Explore a single string all the way through. Recursively add every level where 
     * we find a new string.
     * @param output
     * @param bases
     * @return 
     */
    private List<Integer> explore(List<Integer> output, String bases) {
        //First, deal with the base case.
        if(bases.length()==0){
            return output;
        }
        
        for(int i=0;i<bases.length();i++){
            String current = bases.substring(i);
            if(found(current, root)){
                output.add(i);
            } 
        }
        return output;
    }

    /**
     * Searches for a single match in the trie.
     * @param bases
     * @return True if we can reach a leaf node in the trie this way, false otherwise.
     */
    private boolean found (String bases, TrieNode node){
        //Base case: we have found a leaf.
        if(node.isLeaf()){
            return true;
        }
        //Base case: we're out of bases.
        if(bases.length()==0){
            return false;
        }
        TrieNode current = node.explore(bases.charAt(0));
        //There is no node for the sequence we want.
        if(current ==null){
            return false;
        }
        String outString;
        if(bases.length()==1){
            outString = "";
        }
        else{
            outString = bases.substring(1);
        }
        return found(outString,current);
    }
}
