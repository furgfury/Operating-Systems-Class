package UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import DataHandling.DataPack;
import DataHandling.ProcessScheduler;
import FileHandling.TextFileReader;

public class StateEditor
{
    //constants
    //strings
    final String OPEN = "Open File";
    final String CREATE = "Create File";
    final String SAVE = "Save File";
    final String FILE_NAME = "File Name";
    final String ADD = "Add Process";
    final String DELETE = "Delete Process";
    final String WARNING = "WARNING!";
    final String FILE_WARNING = "Create or open a new file to add process.";
    final String SAVE_WARNING = "Exit without saving?";
    final String[] COMPONENT_LABELS = {"ID","State","Memory","Scheduling","Accounting","Process","Parent","Children","Files","Resources","CPU Required","Arrival Time"};
    final String TXT_EXTENSION = ".txt";
    final String[] SCHEDULING_ALGORITHMS = {"Select Scheduling","First Come First Serve", "Shortest Job First"};

    //integers
    final int NUM_CPU_PARAMETERS = COMPONENT_LABELS.length;
    final int TEXTFIELD_COLUMNS = 10;
    final int TEXTFIELD_PANEL_ROWS = 5;
    final int TEXTFIELD_PANEL_COLS = 2;
    final int BUTTON_PANEL_ROWS = 1;
    final int TEXTAREA_ROWS = 12;
    final int TEXTAREA_COLS = 40;
    final int FIRST_INT_VALUES = 4;
    //components
    final JLabel EMPTY = new JLabel();
    //system properties
    final String FILE_SEPARATOR = System.getProperty("file.separator");
    final String CURRENT_DIRECTORY = System.getProperty("user.home") + FILE_SEPARATOR + "Desktop" + FILE_SEPARATOR;

    //declare window and subpanels
    JFrame frame;
    JPanel textFieldPanel, buttonPanel, bottomPanel;

    //declare components within the window
    JFileChooser fileChooser;
    JButton open, save, add, delete;
    JTextArea textArea;
    JTextField fileName;
    JComboBox<String> schedulingAlg;

    JTextField[] components = new JTextField[NUM_CPU_PARAMETERS];
    JLabel[] componentLabels = new JLabel[NUM_CPU_PARAMETERS];

    ArrayList<DataPack> dataList = new ArrayList<DataPack>();

    String fullText = "";
    boolean saved = false;

    TextFileReader fileReader;
    FileWriter fileWriter;

    File file = null;

    public StateEditor(int width, int height, String title)
    {
        System.out.println(CURRENT_DIRECTORY);
        //initialize window
        frame = new JFrame(title);
        frame.setSize(new Dimension(width, height));

        //check if user saved before exiting
        frame.addWindowListener(new WindowListener()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                if(!saved)
                {
                    int reply = JOptionPane.showConfirmDialog(frame, SAVE_WARNING, WARNING, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if(reply == JOptionPane.YES_OPTION)
                    {
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    }
                    else
                    {
                        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    }
                }
                else
                {
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
            }

            public void windowOpened(WindowEvent e) {}
            public void windowClosed(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowActivated(WindowEvent e) {}
            public void windowDeactivated(WindowEvent e) {}
        });

        //initialze subpanels
        textFieldPanel = new JPanel();
        buttonPanel = new JPanel();
        bottomPanel = new JPanel();
        
        //initialize buttons
        open = new JButton(OPEN);
        save = new JButton(CREATE);
        add = new JButton(ADD);
        delete = new JButton(DELETE);
        
        //intialize text
        for(int i = 0; i < components.length; i++)
        {
            components[i] = new JTextField(TEXTFIELD_COLUMNS);
            componentLabels[i] = new JLabel(COMPONENT_LABELS[i]);

            textFieldPanel.add(componentLabels[i]);
            textFieldPanel.add(components[i]);
        }

        textArea = new JTextArea(TEXTAREA_ROWS, TEXTAREA_COLS);
        textArea.setEditable(false);

        fileName = new JTextField(FILE_NAME, TEXTFIELD_COLUMNS);

        fileChooser = new JFileChooser(CURRENT_DIRECTORY);

        //add action listener to open button so that when it is pressed it opens a file chooser
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //opens file chooser
                fileChooser.showOpenDialog(frame);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                
                file = fileChooser.getSelectedFile();

                //if file isn't chosen, ignore
                if(file != null)
                {
                    try
                    {
                        save.setText(SAVE);

                        reread();

                        fileName.setEnabled(false);

                        textArea.setText(fullText);
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
                    if(file == null)
                    {
                        file = new File(CURRENT_DIRECTORY + FILE_SEPARATOR + fileName.getText() + TXT_EXTENSION);
                        file.createNewFile();

                        save.setText(SAVE);
                        fileName.setEnabled(false);
                    }
                    
                    fullText = textArea.getText();

                    fileWriter = new FileWriter(file);

                    fileWriter.write(fullText);
                    fileWriter.close();

                    saved = true;
                }
                catch (IOException e1) { e1.printStackTrace(); }
            }
        });

        //add action listener to add button so that when it is pressed it pushes new cpu state to the stack
        add.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(validateStateInformation() && file != null)
                {
                    String[] tempData = getStateInformation().split(" ");
                    DataPack temp = new DataPack(tempData);

                    dataList.add(temp);

                    fullText = getStateInformation() + fullText;

                    textArea.setText(fullText);
                }
                else
                {
                    JOptionPane.showMessageDialog(frame, FILE_WARNING, WARNING, JOptionPane.WARNING_MESSAGE);
                }

                saved = false;
            }
        });
        //add action listener to delete button so that when it is pressed it pops new cpu state from the stack
        delete.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                boolean nextLine = false;

                for(int i = 0; i < fullText.length() - 1; i++)
                {
                    if(fullText.substring(i, i + 1).contains("\n"))
                    {
                        nextLine = true;
                    }

                    if(nextLine)
                    {
                        fullText = fullText.substring(i + 1, fullText.length());
                        break;
                    }
                }

                textArea.setText(fullText);

                saved = false;
            }
        });

        schedulingAlg = new JComboBox<String>(SCHEDULING_ALGORITHMS);

        schedulingAlg.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int scheduleAlg = 0;

                switch(schedulingAlg.getSelectedItem().toString())
                {
                    case "First Come First Serve":
                        scheduleAlg = 1;
                    case "Shortest Job First":
                        scheduleAlg = 2;
                }

                reschedule(scheduleAlg);
                
                // try { reread(); }
                // catch (IOException e1) { e1.printStackTrace(); }
            }
        });

        //add buttons to button subpanel
        buttonPanel.add(add);
        buttonPanel.add(delete);
        buttonPanel.add(schedulingAlg);
        buttonPanel.add(open);
        buttonPanel.add(save);
        buttonPanel.add(fileName);
        
        //add subpanels to bottom panel
        bottomPanel.add(textArea);
        bottomPanel.add(buttonPanel);

        //add text and button subpanels to window
        frame.add(textFieldPanel);
        frame.add(bottomPanel);

        //format the window and subpanels to look organized
        frame.setLayout(new FlowLayout());
        textFieldPanel.setLayout(new GridLayout(NUM_CPU_PARAMETERS/TEXTFIELD_PANEL_COLS,TEXTFIELD_PANEL_COLS));
        buttonPanel.setLayout(new GridLayout(buttonPanel.getComponentCount(), BUTTON_PANEL_ROWS));
        bottomPanel.setLayout(new FlowLayout());

        //frame housekeeping
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    //validates the information so no characters can be placed where ints are only allowed
    private boolean validateStateInformation()
    {
        for(int i = 0; i < FIRST_INT_VALUES; i++)
        {
            StringBuilder builder = new StringBuilder();

            char[] chars = components[i].getText().toCharArray();

            for(int j = 0; j < chars.length; j++)
            {
                if(Character.isDigit(chars[j]))
                {
                    builder.append(chars[j]);
                }
            }

            components[i].setText(builder.toString());
        }

        return true;
    }

    //compiles all textfield information into a coherent line to push to stack
    private String getStateInformation()
    {
        String temp = "";

        for(int i = 0; i < components.length; i++)
        {
            temp += components[i].getText() + " ";
        }

        return temp + "\n";
    }

    //reads the entire file and stores it in fulltext to reference
    private void reread() throws IOException
    {
        fullText = "";

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;
        while((line = reader.readLine()) != null)
        {
            DataPack temp = new DataPack(line.split(" "));
            dataList.add(temp);

            fullText += temp.getDataString() + "\n";
        }

        reader.close();
    }

    private void reschedule(int scheduleAlg)
    {
        fullText = "";
        
        switch(scheduleAlg)
        {
            case 1:
                ProcessScheduler.firstComeFirstServeScheduler(dataList);
            case 2:
                ProcessScheduler.shortestJobFirstScheduler(dataList);
        }

        for(int i = 0; i < dataList.size(); i++)
        {
            fullText += dataList.get(i).getDataString();
            fullText += "\n";
        }

        textArea.setText(fullText);
    }

    private void randomPCB(int numRandomPCB)
    {
        
    }
}