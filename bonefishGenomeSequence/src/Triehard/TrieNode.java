package Triehard;

/**
 *
 * @author Kris
 */
public class TrieNode {
    /*
    Note: 
        0 - A
        1 - T
        2 - C
        3 - G
        4 - *
    */
    private final TrieNode[] children;
    private final char pathSymbol;
    private static int nodeCount =0;
    private final int nodeID;
    
    /**
     * This constructor should be used only to make the root of a Trie.
     */
    public TrieNode(){
        nodeID = nodeCount++;
        pathSymbol = '!';
        children = new TrieNode[5];
    }
    
    public TrieNode(char symbol,int parentNode){
        nodeID = nodeCount++;
        pathSymbol = symbol;
        children = new TrieNode[5];
        String outputText = ""+parentNode+"->"+nodeID+":"+symbol+"\n";
        SimpleTrie.addOutputLine(outputText);
    }
    /**
     * Creates a new child node at the right level, or returns the existing one at that level.
     * @param symbol
     * @return node coresponding to the right base.
     */
    public TrieNode addChild(char symbol){
        switch(symbol){
            case 'A':
                if(children[0]==null){
                    children[0] = new TrieNode('A',nodeID);
                }
                return children[0];
            case 'T':
                if(children[1]==null){
                    children[1] = new TrieNode('T',nodeID);
                }
                return children[1];
            case 'C':
                if(children[2]==null){
                    children[2] = new TrieNode('C',nodeID);
                }
                return children[2];
            case 'G':
                if(children[3]==null){
                    children[3]= new TrieNode('G',nodeID);
                }
                return children[3];
            default:
                if(children[4]==null){
                    children[4] = new TrieNode('*',nodeID);
                }
                return children[4];
        }
    }
    
    public char getSymbol(){
        return pathSymbol;
    }
       
    
    
    static void reset(){
        nodeCount =0;
    }
}
