package FileHandling;

import java.io.*;
import java.util.Arrays;

public class TextFileReader
{
    // public static void main(String[] args) throws IOException
    // {
    //     int[] indices = {1, 3, 7};
    //     TextFileReader t = new TextFileReader("exercise coordinates.txt");

    //     //String[] d = t.readLineOmit(0, ',');

    //     for(int i = 0; i < indices.length; i++)
    //     {
    //         //System.out.println(d[i]);
    //     }
    // }

    private static File textFile;

    private static BufferedReader reader;

    public TextFileReader(String filePath) throws FileNotFoundException
    {
        textFile = new File(filePath);
        reader = new BufferedReader(new FileReader(textFile));
    }

    //reads line in text file, returns the full string
    public String readLine(int lineIndex) throws IOException
    { 
        String line = "";

        int[] one = { lineIndex };

        line = readLine(one)[0];

        return line;
    }

    //same as readLine, but operates on indices of lines specified in input array
    public String[] readLine(int[] lineIndex) throws IOException
    {
        Arrays.sort(lineIndex);

        String[] line = new String[lineIndex.length];
        int i = 0;
        int j = 0;

        while((line[j] = reader.readLine()) != null)
        {
            if(lineIndex[j] - 1 == i)
            {
                if(j >= lineIndex.length - 1)
                    return line;
                j++;
            }
            i++;
        }

        return line;
    }
    

    //omits reader from returning a char or string and splits string
    //into array around that value, good for reading lists separated by commas
    //for example
    public String[] readLineOmit(int lineIndex, String omitted) throws IOException
    { 
        String line = readLine(lineIndex);

        String[] separated = line.split(omitted);

        return separated;
    }

    //same as readLineOmit, but trashes one or more indices and returns the rest
    //public static String[] readLineOmitTrashCompact(int lineIndex, String omitted, int trashIndex){ return null; };
    //public static String[] readLineOmitTrashCompact(int lineIndex, String omitted, int[] trashIndex){ return null; };
    
    //same as readLineOmitTrash, but operates on indices of lines specified in input array
    //public static String[] readLineOmitTrashCompact(int[] lineIndex, String omitted, int trashIndex){ return null; };
    //public static String[] readLineOmitTrashCompact(int[] lineIndex, String omitted, int[] trashIndex){ return null; };
    

    //reads line and returns array of chars
    public char[] readChars(int lineIndex) throws IOException
    { 
        String line = readLine(lineIndex);

        char[] temp = splitChars(line);

        return temp;
    }

    private static char[] splitChars(String line)
    {
        char[] temp = new char[line.length()];

        for(int i = 0; i < line.length() - 1; i++)
        {
            line.getChars(i, i + 1, temp, 0);
        }

        return temp;
    }


    // public char[][] readChars(int[] lineIndex)
    // { 
    //     char[][] temp = new char[lineIndex.length][];

    //     return null;
    // }


    //removes elements of array at index, then compacts array so it has no empty spaces
    public boolean trashCompact(int index, String[] array)
    { 
        return false;
    }
    public boolean trashCompact(int index, char[] array)
    { 
        return false;
    }

    public boolean trashCompact(int[] index, String[] array)
    { 
        return false;
    }
    public boolean trashCompact(int[] index, char[] array)
    { 
        return false;
    }


    //removes elements of array at index, but keeps empty spaces where values were removed
    public boolean erase(int index, String[] array)
    { 
        return false;
    }
    public boolean erase(int index, char[] array)
    { 
        return false;
    }

    public boolean erase(int[] index, String[] array)
    { 
        return false;
    }
    public boolean erase(int[] index, char[] array)
    { 
        return false;
    }
}