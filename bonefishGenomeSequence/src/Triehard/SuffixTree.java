/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Triehard;

import Model.Sequence;
import java.util.List;

/**
 *
 * @author Kris
 */
public class SuffixTree implements BasicTrie{
    private int size;
    private final SuffixNode root;
    
    public SuffixTree(){
        size =0;
        SuffixNode.reset();
        root = new SuffixNode(null,"",-1);
        
    }

    /**
     * Stores every possible suffix of a given sequence in the trie using the most memory-efficent method possible.
     * This is not nessisarily time efficent.
     * @param s The sequence about to be added to the tree. 
     */
    @Override
    public void addSequence(Sequence s) {
        size++;
        for(int i=s.getBases().length()-1;i>=0;i--){
            String current = s.getBases().substring(i);
            if(current.length()>=27){
                System.out.println("debug");
            }
            root.addChild(current,(i+1));
        }
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
        return root.toString();
    }

    @Override
    public int size() {
        return size;
    }
    
    public List<String> toArray(){
        return root.toArray();
    }
    
}
