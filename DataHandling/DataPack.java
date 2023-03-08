package DataHandling;
public class DataPack
{
    private final int NUMBER_OF_INT_VARS = 5;
    private final int NUMBER_OF_STR_VARS = 5;

    int[] intData = new int[NUMBER_OF_INT_VARS];
    String[] strData = new String[NUMBER_OF_STR_VARS];
    int cpuRequired = 0;
    int arrivalTime = 0;

    public String[] totalData = new String[12];

    public DataPack(String[] data)
    {
        this.intData = getInts(data, 0, NUMBER_OF_INT_VARS);
        this.strData = getStr(data, NUMBER_OF_INT_VARS, NUMBER_OF_STR_VARS);
        
        this.cpuRequired = Integer.parseInt(data[10]);
        this.arrivalTime = Integer.parseInt(data[11]);

        totalData = consolidateData();
    }

    public String getDataString()
    {
        String temp = "";
        
        for(int j = 0; j < totalData.length; j++)
        {
            temp += totalData[j] + " ";
        }
        return temp;
    }

    private String[] consolidateData()
    {
        String[] temp = new String[12];

        for(int i = 0; i < intData.length; i++)
        {
            temp[i] = intData[i] + "";
        }

        for(int i = 0; i < strData.length; i++)
        {
            temp[i + intData.length] = strData[i];
        }

        temp[10] = cpuRequired + "";
        temp[11] = arrivalTime + "";

        return temp;
    }

    private int[] getInts(String[] data, int start, int end)
    {
        int[] temp = new int[NUMBER_OF_INT_VARS];

        for(int i = start; i < end; i++)
        {
            temp[i] = Integer.parseInt(data[i]);
        }

        return temp;
    }

    private String[] getStr(String[] data, int start, int end)
    {
        String[] temp = new String[NUMBER_OF_STR_VARS];

        for(int i = start; i < end; i++)
        {
            temp[i] = data[i];
        }

        return temp;
    }
}