package src.TrieSquencer;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
    public SequenceHolder runStartAgainstTrie(String start)
    {
        StringBuilder searchString = new StringBuilder(start.substring(2));
        StringBuilder toReturn = new StringBuilder(start);
        NodeHolder nodeHolder = getChildOfPath(searchString.toString());
        if(nodeHolder.isError())
        {
            //only in here when the string that we thought was a start does not have a path in the trie.
            //only errors reads will be here
            return new SequenceHolder("start does not have childOfPath", false);
        }
        nodeHolder = nodeHolder.getNode().getOnlyChild();
        toReturn.append(nodeHolder.getNode().getId());
        boolean working = true;
        while (working)
        {
            searchString.delete(0,1);
            searchString.append(nodeHolder.getNode().getId());
            nodeHolder = getChildOfPath(searchString.toString());
            if(nodeHolder.isError())
            {
                System.out.println(toReturn.length());
                return new SequenceHolder(toReturn.toString());
            }
            else
            {
                nodeHolder = nodeHolder.getNode().getOnlyChild();
            }
            if(nodeHolder.isError())
            {
                return new SequenceHolder(nodeHolder.getMessage(), false);
            }
            if(nodeHolder.getNode().getNumberOfChildren() > 1)
            {
                //If we do find ourselves in here we need to add error checking as the next step.
                //I would say take the high quality read. if they are the same... maybe some type if banch.
                System.out.println("In runStartAgainstTrie ERROR multiple children found " + nodeHolder.getNode().getNumberOfChildren());
                working = false;
            }
            else
            {
                toReturn.append(nodeHolder.getNode().getId());
            }
        }
        System.out.println("returning");
        return new SequenceHolder("reached end of run", false);
    }



    //this method returns a node that you find from following the string passed in.
    @Nullable
    private NodeHolder getChildOfPath(@NonNull String s)
    {
        NodeHolder nodeHolder = new NodeHolder(root);
        for (int i = 0; i < s.length(); i++)
        {
            if(!nodeHolder.isError())
            {
                nodeHolder = nodeHolder.getNode().getChild(s.charAt(i));
            }
        }
        return nodeHolder;
    }

    //returns true if the prefix of the string passed in is not in the trie
    //used to find a possible start.
    //**note error reads will also return true.**
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
            else if(node.getChild(s.charAt(i)).isError())
            {
                return true;
            }
            else
            {
                node = node.getChild(s.charAt(i)).getNode();
            }
        }
        return false;
    }
}