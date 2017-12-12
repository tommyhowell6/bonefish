package src.TrieSquencer;

/**
 * Created by tommyhowell on 12/7/17.
 */

public class NodeHolder
{
    private String message;
    private Boolean isError;
    private Node node;

    public NodeHolder(String message)
    {
        isError = true;
        this.message = message;
    }

    public NodeHolder(Node node)
    {
        isError = false;
        this.node = node;
    }

    public String getMessage()
    {
        return message;
    }

    public Boolean isError()
    {
        return isError;
    }

    public Node getNode()
    {
        return node;
    }
}
