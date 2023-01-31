package com.assignment2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The FileMerger class is used to merge multiple .csv files into a single file.
 */
public class FileMerger {

    /*
    * The directory that contains the .csv files to be merged.
    */
    private String directory;

    /**
     * Constructs a new instance of the FileMerger class with the specified directory.
     * 
     * @param directory The directory that contains the .csv files to be merged.
     */
    public FileMerger(String directory) {
        this.directory = directory;
    }

    /**
     * Merges all .csv files in the specified directory into a single .csv file.
     * 
     * @throws IOException If an error occurs while reading or writing the files.
     * @throws FileNotFoundException If the specified directory or a .csv file cannot be found.
     */
    public void mergeFiles(String fileName) throws IOException, FileNotFoundException {
        boolean isFirst = true;
        try {
            // Get all files with .csv extension in the directory
            File dir = new File(directory);
            File[] csvFiles = dir.listFiles((d, name) -> name.endsWith(".csv"));
        
            // Create a new file to store the merged data
            File mergedFile = new File(directory + "/" + fileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(mergedFile, true));
        
            // Iterate through the csv files and merge the data
            for (File csvFile : csvFiles) {
                BufferedReader reader = new BufferedReader(new FileReader(csvFile));
                String line = null;
                if(!isFirst)
                {
                    line = reader.readLine(); //For every csv except the first, skip the first row because it's a header
                }
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
                reader.close();
                isFirst = false;
            }
            // Close the writer and inform the user that the files have been merged
            writer.flush();
            writer.close();
            System.out.println("CSV files in the directory have been merged into " + mergedFile.getName());
        } catch (IOException e) {
            System.err.println("An error occurred while merging the files: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
