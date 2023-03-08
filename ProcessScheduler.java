package DataHandling;

import java.util.ArrayList;

/**
 * This class is used to schedule processes using the FCFS and SJF algorithms.
 * @author EKKOING
 * @version 1.0
 * @Date 20230303
 */
public class ProcessScheduler {
    
    /** 
     * Schedules the processes using the FCFS algorithm.
     * @param data The Process Data to be scheduled
     */
    public static void firstComeFirstServeScheduler(ArrayList<DataPack> data) {
        mergeSort(data, "fcfs");
    }

    
    /** 
     * Schedules the processes using the SJF algorithm.
     * @param data The Process Data to be scheduled
     */
    public static void shortestJobFirstScheduler(ArrayList<DataPack> data) {
        mergeSort(data, "sjf");
    }

    
    /** 
     * Sorts the data using the merge sort algorithm.
     * @param dataPacks The data to be sorted
     * @param sortType The type of sort to be used. Use "fcfs" for first come first serve and "sjf" for shortest job first.
     */
    private static void mergeSort(ArrayList<DataPack> dataPacks, String sortType) {
        if (dataPacks == null || dataPacks.size() <= 1) {
            return;
        }
        ArrayList<DataPack> left = new ArrayList<>();
        ArrayList<DataPack> right = new ArrayList<>();
        int middle = dataPacks.size() / 2;
        for (int i = 0; i < middle; i++) {
            left.add(dataPacks.get(i));
        }
        for (int i = middle; i < dataPacks.size(); i++) {
            right.add(dataPacks.get(i));
        }
        mergeSort(left, sortType);
        mergeSort(right, sortType);

        if (sortType.equals("fcfs")) {
            mergeFCFS(left, right, dataPacks);
        } else if (sortType.equals("sjf")) {
            mergeSJF(left, right, dataPacks);
        } else {
            throw new IllegalArgumentException("Invalid sort type! Use fcfs or sjf.");
        }
    }

    
    /** 
     * Merges the data using the FCFS algorithm.
     * @param left The left side of the data
     * @param right The right side of the data
     * @param dataPacks The data to be sorted
     */
    private static void mergeFCFS(ArrayList<DataPack> left, ArrayList<DataPack> right, ArrayList<DataPack> dataPacks) {
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < left.size() && j < right.size()) {
            if (left.get(i).arrivalTime <= right.get(j).arrivalTime) {
                dataPacks.set(k, left.get(i));
                i++;
            } else {
                dataPacks.set(k, right.get(j));
                j++;
            }
            k++;
        }
        while (i < left.size()) {
            dataPacks.set(k, left.get(i));
            i++;
            k++;
        }
        while (j < right.size()) {
            dataPacks.set(k, right.get(j));
            j++;
            k++;
        }
    }

    
    /** 
     * Merges the data using the SJF algorithm.
     * @param left The left side of the data
     * @param right The right side of the data
     * @param dataPacks The data to be sorted
     */
    private static void mergeSJF(ArrayList<DataPack> left, ArrayList<DataPack> right, ArrayList<DataPack> dataPacks) {
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < left.size() && j < right.size()) {
            if (left.get(i).cpuRequired <= right.get(j).cpuRequired) {
                dataPacks.set(k, left.get(i));
                i++;
            } else {
                dataPacks.set(k, right.get(j));
                j++;
            }
            k++;
        }
        while (i < left.size()) {
            dataPacks.set(k, left.get(i));
            i++;
            k++;
        }
        while (j < right.size()) {
            dataPacks.set(k, right.get(j));
            j++;
            k++;
        }
    }
}