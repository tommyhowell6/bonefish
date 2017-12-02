package Triehard;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kris
 */
public class SuffixNode {
        /*
    Note: 
        0 - A
        1 - T
        2 - C
        3 - G
        4 - $
        5 - *
    
    */
    private final SuffixNode[] children;
    private boolean isLeaf;
    private String parentEdge;
    private final SuffixNode parent;
    private int startIndex;
    private static int nodeCounter = -1;
    private final int nodeID;
    
    public SuffixNode(SuffixNode parent, String parentEdge, int startIndex){
        nodeID = nodeCounter;
        this.parentEdge=parentEdge;
        this.parent=parent;
        isLeaf = true;
        children = new SuffixNode[6];
        this.startIndex = startIndex;
        //System.out.println("Node created with edge: "+parentEdge);
        nodeCounter++;
    }
    
    public static int getNodes(){
        return nodeCounter;
    }
    
    public static void reset(){
        nodeCounter =-1;
    }
    
    public boolean isLeaf(){
        return this.isLeaf;
    }
    
    private int getChildId(char charAt) {
        switch(charAt){
           case 'A':
                return 0;
            case 'T':
                return 1;
            case 'C':
                return 2;
            case 'G':
                return 3;
            default:
                return 5;
        }
    }
    
    /**
     * This node will explore down this node, possibly breaking itself into a new node to return to the parent to replace itself with.
     * This may mean breaking itself and adding both itself and the new string at the appropriate point.
     * This is where the magic happens.
     * @param match String that matches at least some portion of the "parentEdge" of this node, possibly only one letter.
     * @return a new node for the parent to use in place of this one.
     */
    void addChild(String current, int i) {
        /**
         * First, handle the base case that we're in the root.
         * In those cases, we will always pass down the line for children to worry
         * about how to merge.
         */
        if(isRoot()){
            System.out.println("\t\t\tWe think we're root.");
            makeOrPassChild(current, i);
        }
        /**
         * In the vast majority of cases, we will not be in the root. This means 
         * that we need to:
         * 1. Discover where the degree of overlap (will be at least 1). 
         * 2. Modify our own edge to keep only that overlap.
         * 3. Add new children to ourselves coresponding to the pieces that aren't in common.
         */
        else{
            System.out.println("Moving down existing edge.");
            System.out.println("Edge currently: "+parentEdge);
            System.out.println("Input edge      : "+current);
            String overlap ="";
            int overlapIndex;
            //Note: At least the first character should always overlap.
            for(overlapIndex=0;overlapIndex<current.length()&&overlapIndex<parentEdge.length();overlapIndex++){
                if(parentEdge.charAt(overlapIndex)==current.charAt(overlapIndex)){
                    overlap+=parentEdge.charAt(overlapIndex);
                }
                else{
                    break;
                }
            }  
            String pastCutoff = parentEdge.substring(overlapIndex);
            System.out.println("Keeping      : "+overlap);
            /**
             * We need to split the current node.
             */
            if(pastCutoff.length()>0){
                System.out.println("\tExisting node must be split!");
                System.out.println("Split at     : "+pastCutoff);
                
                parentEdge = parentEdge.substring(0, overlapIndex);
                System.out.println("This edge is now: "+parentEdge);
                makeOrPassChild(pastCutoff, startIndex);
                startIndex = -1;
                System.out.println("\tFinished splitting existing node!");
            }
            else{
                System.out.println("Parent node did not have to be split, we just move down.");
            }
            //Now with the newly added node.
            pastCutoff = current.substring(overlapIndex);
            
            if(pastCutoff.length()>0){
                System.out.println("New Edge to be Added: "+pastCutoff+".");
                makeOrPassChild(pastCutoff,i);
            }
            else{
                System.out.println("That's it. What we were adding overlapped perfectly with this section.");
            }
            
            System.out.println("Split complete.");
        } 
        isLeaf = false;
    }
    
    private boolean isRoot(){
        return (parent==null);
        //return (parentEdge.length()==0);
    }

    /**
     * Makes a new child if we need one, otherwise we'll just pass the newly broken section down the chain.
     * @param pastCutoff
     * @param startIndex 
     */
    private void makeOrPassChild(String current, int start) {
        //TODO: This is a serious point of failure. Something fishy is going on here.
        if(parentEdge.equals(current)){
            System.out.println("Tried to add a node equal to myself.");
            return;
        }

        int id = getChildId(current.charAt(0));
            SuffixNode child = children[id];
            //We don't have a node coresponding to the first letter of this string yet.
            if(child==null){
                children[id] = new SuffixNode(this, current, start);
            }
            //We do. This means we have to pass the operation down the chain        
            else{
                child.addChild(current,start);
            }
    }
    
    @Override
    public String toString(){
        String output = parentEdge+"\n";
        for(SuffixNode child:children){
            if(child!=null){
                output+=child.toString();
            }
        }
        return output;
    }
    
    public List<String> toArray(){
        ArrayList<String> output = new ArrayList<>();
        if(!parentEdge.equals("")){
            output.add(parentEdge);
        }
        
        for(SuffixNode child:children){
            if(child!=null){
                output.addAll(child.toArray());
            }
        }
        return output;
    }
    
    private String drawLinks(){
        if(isRoot()){
            return "";
        }
        String output =parentEdge +"<-";
        if(parent!=null){
            output+=parent.drawLinks();
        }
        return output;
    }
}
