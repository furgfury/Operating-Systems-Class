package UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

import FileHandling.TextFileReader;
import FileHandling.TextFileWriter;

public class PCB
{
    //declare window and subpanels
    JFrame frame;
    JPanel textFieldPanel, buttonPanel;

    //declare components within the window
    JFileChooser fileChooser;
    JButton open, save, nextLine, prevLine;
    JLabel[] infoLabel = new JLabel[9];
    JTextField[] info = new JTextField[9];

    //constants
    //labels for buttons
    final String OPEN = "Open File";
    final String SAVE = "Save File";
    final String NEXT_LINE = "Next Line";
    final String PREV_LINE = "Previous Line";

    String fullText = "";

    int fileLine = 1;

    TextFileReader fileReader;

    // info text for each button
    String[] infoText = {"ID","CPU State","Memory","Scheduling Information","Accounting Information","Process State","Parent","Children","Open Files","Other Resources"};
    File file = null;

    public PCB(int width, int height, String title)
    {
        //initialize window
        frame = new JFrame(title);
        frame.setSize(new Dimension(width, height));

        //initialze subpanels
        textFieldPanel = new JPanel();
        buttonPanel = new JPanel();

        //initialize buttons
        open = new JButton(OPEN);
        save = new JButton(SAVE);
        nextLine = new JButton(NEXT_LINE);
        prevLine = new JButton(PREV_LINE);

        fileChooser = new JFileChooser();

        //add action listener to open button so that when it is pressed it opens a file chooser
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //opens file chooser
                fileChooser.showOpenDialog(frame);
                file = fileChooser.getSelectedFile();
                
                //if file isn't chosen, ignore
                if(file != null)
                {
                    try
                    {
                        updateInformation(file);

                        reread();
                    } catch (IOException e1) {e1.printStackTrace();}
                }
            }
        });

        //add action listener to save button so that when it is pressed it writes all data back into file
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    TextFileWriter writer = new TextFileWriter(file);

                    String updatedInfo = "EMPTY";

                    for(int i = 0; i < info.length; i++)
                    {
                        updatedInfo += " " + info[i].getText();
                    }

                    rewrite(updatedInfo, fileLine);

                    writer.write(fullText);
                }
                catch (IOException e1) { e1.printStackTrace(); }
            }
        });

        //add action listener to nextLine button so that it populates the gui with the next line in the file
        nextLine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                fileLine += 1;

                try {updateInformation(file);} catch (IOException e1) {e1.printStackTrace();}
            }
        });

        //add action listener to prevLine button so it populates the gui with the previous line
        prevLine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                fileLine -= fileLine == 1 ? 0 : 1;

                try {updateInformation(file);} catch (IOException e1) {e1.printStackTrace();}
            }
        });
        
        //initialize all labels and textfields, add them to text subpanel
        for(int i = 0; i < info.length; i++)
        {
            infoLabel[i] = new JLabel(infoText[i]);
            info[i] = new JTextField(10);

            textFieldPanel.add(infoLabel[i]);
            textFieldPanel.add(info[i]);
        }

        //add buttons to button subpanel
        buttonPanel.add(open);
        buttonPanel.add(save);
        buttonPanel.add(nextLine);
        buttonPanel.add(prevLine);

        //add text and button subpanels to window
        frame.add(textFieldPanel);
        frame.add(buttonPanel);

        //format the window and subpanels to look organized
        frame.setLayout(new FlowLayout());
        textFieldPanel.setLayout(new GridLayout(5,2));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, 1));

        //frame housekeeping
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    //reads the line of information in the file, returning an array of strings
    //and populates the text fields appropriately
    private void updateInformation(File file) throws IOException
    {
        try
        {
            String[] processInformation = new String[info.length];

            fileReader = new TextFileReader(file.toPath().toString());

            try
            {
                //splits line into array of strings among every space
                processInformation = fileReader.readLineOmit(fileLine, " ");
            }
            catch(Exception e)
            {
                for(int i = 0; i < info.length; i++)
                {
                    processInformation[i] = "";
                }
            }

            for(int i = 0; i < info.length; i++)
            {
                info[i].setText(processInformation[i]);
            }

            reread();
        }
        catch (FileNotFoundException e1) { e1.printStackTrace(); }
    }

    //reads the entire file and stores it in fulltext to reference
    public void reread() throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        while(reader.readLine() != null)
        {
            fullText += reader.readLine() + "\n";
        }

        reader.close();
    }

    //rereads file and stores it into fullText, inserts new line of information at index i withiin it
    public void rewrite(String insertText, int index) throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        int i = 0;
        while(reader.readLine() != null)
        {
            if(i != index)
                fullText += reader.readLine() + "\n";
            else
                fullText += insertText + "\n";
            i++;
        }

        reader.close();
    }
}