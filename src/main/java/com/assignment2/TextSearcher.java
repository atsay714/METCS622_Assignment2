package com.assignment2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The TextSearcher class performs operations to search for a specific text in a file
 */
class TextSearcher {
    private String filePath;

    public TextSearcher(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Search for the specified search string in the file at the given file path.
     * 
     * @param filePath the file path to search in
     * @param searchString the string to search for
     * @return true if the search string is found in the file, false otherwise
     * @throws IOException if there is an error reading the file
     */
    public void searchForText(String searchString) throws IOException {
        BufferedReader reader = null;
        try {
            String searchWord = searchString;
            String line;
            
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                int count = 0;
                
                boolean isHeader = true;
                while ((line = br.readLine()) != null) {
                    if(isHeader){
                        isHeader = false;
                        continue;
                    }
                    
                    List<String> data = new ArrayList<>();
                    data = SplitLine(line);

                    count += data.size();
                    
                    while(count < 25) {
                        String line2 = br.readLine();
                        line += line2;
                        List<String> data2 = new ArrayList<>();
                        data2 = SplitLine(line);
                        count = data2.size();
                        if(count == 25) {
                            data = data2;
                        }
                    }

                    if(count == 25)
                    {
                        for (int i = 0; i < data.size(); i++) {
                            data.set(i,data.get(i).replaceAll("^\"|\"$", ""));
                        }
                        if (data.get(data.size() - 1).contains(searchWord)) {
                            System.out.println("Title: " + data.get(data.size() - 1) + ". Funds Raised Percent: " + data.get(7) + ". Close Date: " + data.get(4));
                        }
                        count = 0;
                        data = new ArrayList<>();
                    }
                    else if(count > 25){
                        throw new IOException("Error reading a line");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found at " + filePath);
        } catch (IOException e) {
            System.out.println("Error: Unable to read from file at " + filePath);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

   /**
    * Splits a given line of CSV text into individual elements, separated by commas.
    * Handles cases where elements contain commas within quotes by ignoring the comma.
    * @param line a line of CSV text to be split
    * @return a list of individual elements from the line
    */
    private List<String> SplitLine(String line) {
        StringBuilder column = new StringBuilder();
        boolean insideQuotes = false;
        List<String> data = new ArrayList<>();

        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '\"') {
                insideQuotes = !insideQuotes;
            } else if (line.charAt(i) == ',' && !insideQuotes) {
                data.add(column.toString());
                column.setLength(0);
                continue;
            }
            column.append(line.charAt(i));
        }
        data.add(column.toString());
        return data;
    }
}