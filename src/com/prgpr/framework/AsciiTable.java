package com.prgpr.framework;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by kito on 05.12.16.
 *
 * A class for generating auto scaling ascii tables for printing data
 * to the console.
 *
 * @author Kyle Rinfreschi
 */
public class AsciiTable {
    private String separator = "|";  // Separates items in one row
    private Set<String> columns = new LinkedHashSet<>();
    private List<Object[]> rows = new LinkedList<>();

    public AsciiTable(){
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    /**
     * Sets the header of the table
     *
     * @param cols A number of strings representing the header row
     */
    public void setColumns(String... cols){
        columns.clear();
        clear();
        Stream.of(cols)
                .forEachOrdered((col) -> columns.add(col));
    }

    /**
     * Adds a row of data underneath the last added row.
     *
     * @param cols The data to be put into the row in the same order as was specified in setColumns
     * @throws RuntimeException If the passed row is too wide or narrow according to setCloumns
     */
    public void addRow(Object... cols) throws RuntimeException {
        if(cols.length != columns.size()){
            throw new RuntimeException("Invalid column length");
        }

        rows.add(cols);
    }

    /**
     * Clears the table of all rows, but keeps the header.
     */
    public void clear(){
        rows.clear();
    }

    /**
     * Prints the table to stdout.
     */
    public void print(){
        System.out.print(toString());
    }

    /**
     * @return A string representation of the tables current state.
     */
    @Override
    public String toString(){
        StringBuilder output = new StringBuilder();
        String baseFormat = calculateFormat(rows);
        String format = baseFormat + "\n";
        String sep = String.format(baseFormat, columns.toArray()).replaceAll("[^|]", "-")
                .replaceAll(String.format("\\%s", separator), "+") + "\n";

        output.append(sep);
        output.append(String.format(format, columns.toArray()));
        output.append(sep);
        rows.stream()
                .map(row -> Stream.of(row).map(s -> s == null ? "" : s).toArray())
                .forEachOrdered(args -> output.append(String.format(format, args)));
        output.append(sep);
        return output.toString();
    }

    /**
     * Returns a formatable string of the appropriate length to accommodate all of the data without overlaps.
     *
     * @param rows The data to be put into the table
     * @return A base format which will fit all rows
     */
    private String calculateFormat(List<Object[]> rows) {
        Integer[] maximums = columns.stream()
                                .map(String::length)
                                .toArray(Integer[]::new);
        rows.stream().map(row -> {
            return Stream.of(row)
                    .map(String::valueOf)
                    .map(String::length);
        }).forEach((sizes) -> {
            int[] idx = { 0 };
            sizes.forEachOrdered((value) -> {
                if(maximums[idx[0]] != null && value <= maximums[idx[0]]) {
                    idx[0]++;
                    return;
                }

                maximums[idx[0]] = value;
                idx[0]++;
            });
        });

        String colFormat = Stream.of(maximums)
                .map(this::itemFormat)
                .reduce((s1, s2) -> String.format("%s %s %s", s1, separator, s2))
                .orElse("");

        return String.format("| %s |", colFormat);
    }

    /**
     * @param s The item to be displayed
     * @return A string long enough for the given item
     */
    private String itemFormat(int s) {
        try {
            return String.format("%%-%ss", s);
        }catch (MissingFormatArgumentException e){
            return "";
        }
    }
}
