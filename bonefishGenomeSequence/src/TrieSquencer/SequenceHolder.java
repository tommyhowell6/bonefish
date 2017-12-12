package src.TrieSquencer;

/**
 * Created by tommyhowell on 12/12/17.
 */

public class SequenceHolder
{
    private String message;
    private Boolean isError;
    private String Sequence;

    public SequenceHolder(String sequence)
    {
        isError = false;
        message = "good sequence";
        Sequence = sequence;
    }

    public SequenceHolder(String message, Boolean isError)
    {
        this.message = message;
        this.isError = isError;
    }

    public String getMessage()
    {
        return message;
    }

    public Boolean isError()
    {
        return isError;
    }

    public String getSequence()
    {
        return Sequence;
    }
}
