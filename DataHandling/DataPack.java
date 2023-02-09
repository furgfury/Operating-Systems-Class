package DataHandling;
public class DataPack
{
    private final int NUMBER_OF_INT_VARS = 5;
    private final int NUMBER_OF_STR_VARS = 5;

    int[] intData = new int[NUMBER_OF_INT_VARS];
    String[] strData = new String[NUMBER_OF_STR_VARS];

    public DataPack(String[] data)
    {
        this.intData = getInts(data);
        this.strData = getStr(data);
    }

    private int[] getInts(String[] data)
    {
        int[] temp = new int[NUMBER_OF_INT_VARS];

        for(int i = 0; i < NUMBER_OF_INT_VARS; i++)
        {
            temp[i] = Integer.parseInt(data[i]);
        }

        return temp;
    }

    private String[] getStr(String[] data)
    {
        String[] temp = new String[NUMBER_OF_STR_VARS];

        for(int i = NUMBER_OF_INT_VARS; i < NUMBER_OF_INT_VARS + NUMBER_OF_STR_VARS; i++)
        {
            temp[i] = data[i];
        }

        return temp;
    }
}