package eulerianSequencer;

/**
 * Created by Jesse on 10/31/2017.
 */
public class PairedReadsInfo {

    private String string1;
    private String string2;
    private String accuracy;
    public PairedReadsInfo()
    {

    }

    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }

    public String getString2() {
        return string2;
    }

    public void setString2(String string2) {
        this.string2 = string2;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }
}
