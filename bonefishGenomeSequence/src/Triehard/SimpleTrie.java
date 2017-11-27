/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Triehard;

import Model.Sequence;
import java.util.Collection;

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
        c.stream().forEach((s) -> {
            addSequence(s);
        });
    }
    

    @Override
    public boolean contains(Sequence s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
    
}
