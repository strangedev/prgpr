package com.prgpr.framework;

import org.neo4j.cypher.internal.compiler.v2_3.commands.expressions.Collect;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by kito on 05.12.16.
 */
public class AsciiTable {
    private String separator = "|";
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

    public void setColumns(String... cols){
        columns.clear();
        Stream.of(cols)
                .forEachOrdered((col) -> columns.add(col));
    }

    public void addRow(Object... cols) throws RuntimeException {
        if(cols.length != columns.size()){
            throw new RuntimeException("Invalid column length");
        }

        rows.add(cols);
    }

    public void print(){
        System.out.print(toString());
    }

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

    private String itemFormat(int s) {
        try {
            return String.format("%%-%ss", s);
        }catch (MissingFormatArgumentException e){
            return "";
        }
    }
}
