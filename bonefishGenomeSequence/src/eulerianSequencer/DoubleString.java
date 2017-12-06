package eulerianSequencer;

/**
 * Created by Jesse on 10/16/2017.
 */
public class DoubleString implements Comparable {
    private String string1;
    private String string2;

    public DoubleString(String string1, String string2)
    {
        this.string1 = string1;
        this.string2 = string2;
    }




    @Override
    public boolean equals(Object object)
    {
        if(object.getClass() != DoubleString.class)
        {
            return false;
        }
        else
        {
            DoubleString theObject = (DoubleString) object;
            if(!theObject.string1.equals(this.string1) || !theObject.string2.equals(this.string2))
            {
                return false;
            }
        }
        return true;

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

    @Override
    public int compareTo(Object o) {
        if(this.equals(o))
        {
            return 0;
        }
        else
        {
            DoubleString newDouble = (DoubleString) o;
            if(string1.compareTo(newDouble.string1) == 1 && string2.compareTo(newDouble.string2) == 1 )
            {
                return 1;
            }
            else if(string1.compareTo(newDouble.string1) == -1 && string2.compareTo(newDouble.string2) == -1 )
            {
                return -1;
            }
            else
            {
                if(string1.compareTo(newDouble.string1) != 0)
                {
                    return string1.compareTo(newDouble.string1);
                }
                else
                {
                    return string2.compareTo(newDouble.string2);
                }
            }
        }

    }
}
