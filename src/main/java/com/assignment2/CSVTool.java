package com.assignment2;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The MergeCSV class provides methods to perform search operation on a CSV file
 * and merge multiple CSV files in a directory.
 */
public final class CSVTool {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private CSVTool() {
    }

    /**
     * The main method that allows the user to choose which operation to perform:
     * search for text in a CSV file or merge multiple CSV files.
     *
     * @param args Command line arguments
     * @throws IOException If an I/O error occurs
     */
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter '1' to search for text or '2' to merge files: ");
        int choice = sc.nextInt();
        try {
            switch (choice) {
                case 1:
                    final String windowsFilePathRegex = "^(?:[a-zA-Z]\\:|\\\\[\\w\\.]+\\\\[\\w.$]+)\\\\(?:[\\w]+\\\\)*\\w([\\w.])+$"; //Regex to match windows file path
                    System.out.println("Enter Windows file path: ");
                    String filePath = sc.next();
                    Matcher windowsFilePathMatcher = createMatcher(windowsFilePathRegex, filePath);

                    if (windowsFilePathMatcher.find()) {
                        System.out.println("Enter what word to search for: ");
                        String word = sc.next();
                        TextSearcher textSearcher = new TextSearcher(filePath);
                        textSearcher.searchForText(word);
                    }
                    else {
                        throw new IllegalArgumentException("Invalid file path");
                    }
                    break;
                case 2:
                    final String windowsDirectoryRegex = "^[a-zA-Z]:\\\\(?:\\w+\\\\?)*$"; //Regex to match windows directory
                    System.out.println("Enter what directory to search for files: ");
                    String directory = sc.next();
                    Matcher windowsDirectoryMatcher = createMatcher(windowsDirectoryRegex, directory);

                    if (windowsDirectoryMatcher.find()) {
                        FileMerger fileMerger = new FileMerger(directory);
                        System.out.println("Enter output file name: ");
                        String fileName = sc.next();
                        try {
                            validateCsvFileName(fileName);
                            System.out.println("File name is valid");
                        } catch (IllegalArgumentException e) {
                            System.out.println("Error: " + e.getMessage());
                            break;
                        }

                        fileMerger.mergeFiles(fileName);
                    } else {
                        throw new IllegalArgumentException("Invalid directory path");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid choice");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            sc.close();
        }
    }

    /**
     * This method creates a Matcher for the specified regex string and input string.
     *
     * @param regexString The regex string to be compiled into a Pattern.
     * @param input The input string to be matched against the regex string.
     *
     * @return A Matcher instance that can be used to match the input string against the regex string.
     */
    private static Matcher createMatcher(String regexString, String input) {
        String windowsDirectoryRegex = regexString;
        Pattern windowsDirectoryPattern = Pattern.compile(windowsDirectoryRegex);
        return windowsDirectoryPattern.matcher(input);
    }

    /**
     * This method checks if the given filename ends with the ".csv" extension.
     *
     * @param filename The name of the file to validate.
     *
     * @throws IllegalArgumentException if the filename does not end with ".csv".
     */
    private static void validateCsvFileName(String filename) throws IllegalArgumentException {
        if (!filename.endsWith(".csv")) {
            throw new IllegalArgumentException("File name must end in .csv");
        }
    }
}
