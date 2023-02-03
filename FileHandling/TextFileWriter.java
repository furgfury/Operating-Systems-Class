package FileHandling;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TextFileWriter
{
    //declare file and writer variables
    File file;
    FileWriter writer;

    //constructor
    public TextFileWriter(File file) throws IOException
    {
        this.file = file;
        writer = new FileWriter(file);
    }

    //write info to whatever file it is attached to
    public void write(String info) throws IOException
    {
        writer.write(info + "\n");
        writer.close();
    }
}