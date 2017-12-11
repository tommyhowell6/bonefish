package src.TrieSquencer;


/**
 * Created by tommyhowell on 11/14/17.
 */

public class GenomeSequencer
{
    Node root;
    private char[] intToCharConverter = "ACGT".toCharArray();
    public GenomeSequencer()
    {
        //R is for root. This should not ever get read
        root = new Node('R');
    }

    public void addNode(String s)
    {
        root.add(s.substring(1));
    }

    //input a string and it will build the longest genome that it can.
    //If it can't find a path on the string passed in it will return without any genome.
    public String runStartAgainstTrie(String start)
    {
        StringBuilder searchString = new StringBuilder(start.substring(2));
        StringBuilder toReturn = new StringBuilder(start);
        Node node = getChildOfPath(searchString.toString());
        if(node == null)
        {
            //only in here when the string that we thought was a start does not have a path in the trie.
            //only errors reads will be here
            return "likely error";
        }
        node = node.getOnlyChild();
        toReturn.append(node.getId());
        boolean working = true;
        while (working)
        {
            searchString.delete(0,1);
            searchString.append(node.getId());
            node = getChildOfPath(searchString.toString());
            if(node == null)
            {
                System.out.println(toReturn.length());
                return toReturn.toString();
            }
            else
            {
                node = node.getOnlyChild();
            }
            if(node.getNumberOfChildren() > 1)
            {
                //If we do find ourselves in here we need to add error checking as the next step.
                //I would say take the high quality read. if they are the same... maybe some type if banch.
                System.out.println("In runStartAgainstTrie ERROR multiple children found " + node.getNumberOfChildren());
                working = false;
            }
            else
            {
                toReturn.append(node.getId());
            }
        }
        System.out.println("returning");
        return toReturn.toString();
    }



    //this method returns a node that you find from following the string passed in.
    private Node getChildOfPath(String s)
    {
        Node node = root;
        for (int i = 0; i < s.length(); i++)
        {
            if(node != null)
            {
                node = node.getChild(s.charAt(i));
            }
        }
        return node;
    }

    //returns true if the prefix of the string passed in is not in the trie
    //used to find a possible start.
    //note error reads will also return true.
    public boolean prefixNotInTrie(String s)
    {
        String pre = s.substring(0,s.length() - 1);
        Node node = root;
        for (int i = 0; i < pre.length(); i++)
        {
            if(node == null)
            {
                return true;
            }
            else if(node.getChildren() == null)
            {
                return true;
            }
            else if(node.getChild(s.charAt(i)) == null)
            {
                return true;
            }
            else
            {
                node = node.getChild(s.charAt(i));
            }
        }
        return false;
    }
}
