package util;

import java.util.*;

public class CsvPrinter {
    private String[] columnNames;

    private List<double[]> rows;

    public CsvPrinter(String[] columnNames) {
        this.columnNames = columnNames;

        rows = new LinkedList<>();
    }

    public String getCsvHeader() {
        return concatenateRow(columnNames);
    }

    private <T> String concatenateRow(T[] row) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < row.length - 1; i++) {
            stringBuilder.append(row[i]);
            stringBuilder.append(',');
        }

        stringBuilder.append(row[row.length - 1]);

        return stringBuilder.toString();
    }

    private String concatenateRow(double[] row) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < row.length - 1; i++) {
            stringBuilder.append(row[i]);
            stringBuilder.append(',');
        }

        stringBuilder.append(row[row.length - 1]);

        return stringBuilder.toString();
    }

    public void addRow(double...orderedValues) {
        rows.add(orderedValues);
    }

    public void addRowAndPrint(double...orderedValues) {
        addRow(orderedValues);
        System.out.println(concatenateRow(orderedValues));
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public double[] getRow(int rowIndex) {
        return rows.get(rowIndex);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(getCsvHeader());

        for (double[] row : rows) {
            stringBuilder.append("\n");

            stringBuilder.append(concatenateRow(row));
        }

        return stringBuilder.toString();
    }
}
