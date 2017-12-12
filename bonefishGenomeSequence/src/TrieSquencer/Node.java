package src.TrieSquencer;

/**
 * Created by tommyhowell on 11/14/17.
 */

public class Node
{
    private Node [] children;
    private int numberOfChildren;
    private int count;
    private char id;

    public Node(char id)
    {
        this.id = id;
        numberOfChildren = 0;
    }


    public void add(String s)
    {
        if(children == null)
        {
            children = new Node[4];
        }
        Node temp;
        switch (s.toUpperCase().charAt(0))
        {
            case 'A':
                if(children[0] == null)
                {
                    temp = new Node('A');
                    numberOfChildren++;
                }
                else
                {
                    temp = children[0];
                }
                if(s.length() > 1)
                {
                    temp.add(s.substring(1));
                }
                else
                {
                    count++;
                }
                children[0] = temp;
                break;
            case 'C':
                if(children[1] == null)
                {
                    temp = new Node('C');
                    numberOfChildren++;
                }
                else
                {
                    temp = children[1];
                }
                if(s.length() > 1)
                {
                    temp.add(s.substring(1));
                }
                else
                {
                    count++;
                }
                children[1] = temp;
                break;
            case 'G':
                if(children[2] == null)
                {
                    temp = new Node('G');
                    numberOfChildren++;
                }
                else
                {
                    temp = children[2];
                }
                if(s.length() > 1)
                {
                    temp.add(s.substring(1));
                }
                else
                {
                    count++;
                }
                children[2] = temp;
                break;
            case 'T':
                if(children[3] == null)
                {
                    temp = new Node('T');
                    numberOfChildren++;
                }
                else
                {
                    temp = children[3];
                }
                if(s.length() > 1)
                {
                    temp.add(s.substring(1));
                }
                else
                {
                    count++;
                }
                children[3] = temp;
                break;
        }

    }

    public int getNumberOfChildren()
    {
        return numberOfChildren;
    }

    public NodeHolder getChild(char c)
    {
        if(children == null)
        {
            return new NodeHolder(id + " had no children in getChild");
        }
        switch (c)
        {
            case 'A':
                if(children[0] != null)
                {
                    return new NodeHolder(children[0]);
                }
                break;
            case 'C':
                if(children[1] != null)
                {
                    return new NodeHolder(children[1]);
                }
                break;
            case 'G':
                if(children[2] != null)
                {
                    return new NodeHolder(children[2]);
                }
                break;
            case 'T':
                if(children[3] != null)
                {
                    return new NodeHolder(children[3]);
                }
                break;
        }
        return new NodeHolder(id + " did not have child " + c + " in getChild");
    }

    public char getId()
    {
        return id;
    }

    public Node[] getChildren()
    {
        return children;
    }


    public NodeHolder getOnlyChild()
    {
        if(children == null)
        {
            return new NodeHolder(id + " had no children in getOnlyChild");
        }
        else if(numberOfChildren != 1)
        {
            return new NodeHolder(id + " had more than one child in getOnlyChild");
        }
        for (int i = 0; i < 4; i++)
        {
            if(children[i] != null)
            {
                return new NodeHolder(children[i]);
            }
        }
        return new NodeHolder(id + " reached end of getOnlyChild");
    }

}

