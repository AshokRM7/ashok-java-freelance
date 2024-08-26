package com.divproject.divAutomation;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ExcelComparatorWithMappingGUI {
    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Excel Comparator with Mapping");
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the panel
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        // Display the frame
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // File 1 label and button
        JLabel file1Label = new JLabel("Select File 1:");
        file1Label.setBounds(10, 20, 80, 25);
        panel.add(file1Label);

        JTextField file1Text = new JTextField(20);
        file1Text.setBounds(100, 20, 165, 25);
        panel.add(file1Text);

        JButton file1Button = new JButton("Browse");
        file1Button.setBounds(270, 20, 80, 25);
        panel.add(file1Button);

        file1Button.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                file1Text.setText(selectedFile.getAbsolutePath());
            }
        });

        // File 2 label and button
        JLabel file2Label = new JLabel("Select File 2:");
        file2Label.setBounds(10, 50, 80, 25);
        panel.add(file2Label);

        JTextField file2Text = new JTextField(20);
        file2Text.setBounds(100, 50, 165, 25);
        panel.add(file2Text);

        JButton file2Button = new JButton("Browse");
        file2Button.setBounds(270, 50, 80, 25);
        panel.add(file2Button);

        file2Button.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                file2Text.setText(selectedFile.getAbsolutePath());
            }
        });

        // Primary key label and text field
        JLabel primaryKeyLabel = new JLabel("Primary Key:");
        primaryKeyLabel.setBounds(10, 80, 80, 25);
        panel.add(primaryKeyLabel);

        JTextField primaryKeyText = new JTextField(20);
        primaryKeyText.setBounds(100, 80, 165, 25);
        panel.add(primaryKeyText);

        // Mapping input fields
        JLabel mappingLabel = new JLabel("Column Mapping (File1:File2):");
        mappingLabel.setBounds(10, 110, 200, 25);
        panel.add(mappingLabel);

        JTextField mappingText = new JTextField(40);
        mappingText.setBounds(200, 110, 250, 25);
        panel.add(mappingText);

        // Compare button
        JButton compareButton = new JButton("Compare");
        compareButton.setBounds(150, 150, 100, 25);
        panel.add(compareButton);

        compareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String file1Path = file1Text.getText();
                String file2Path = file2Text.getText();
                String primaryKey = primaryKeyText.getText();
                String mapping = mappingText.getText();

                if (!file1Path.isEmpty() && !file2Path.isEmpty() && !primaryKey.isEmpty() && !mapping.isEmpty()) {
                    compareExcelFilesWithMapping(file1Path, file2Path, primaryKey, mapping);
                    JOptionPane.showMessageDialog(null, "Comparison Complete!");
                } else {
                    JOptionPane.showMessageDialog(null, "Please select both files, enter the primary key, and provide column mapping.");
                }
            }
        });
    }

    private static void compareExcelFilesWithMapping(String file1Path, String file2Path, String primaryKey, String mapping) {
        try {
            FileInputStream file1 = new FileInputStream(new File(file1Path));
            FileInputStream file2 = new FileInputStream(new File(file2Path));

            Workbook workbook1 = new XSSFWorkbook(file1);
            Workbook workbook2 = new XSSFWorkbook(file2);

            Sheet sheet1 = workbook1.getSheetAt(0);
            Sheet sheet2 = workbook2.getSheetAt(0);

            Map<String, Row> file1Data = new HashMap<>();
            Map<String, Row> matchedRows = new HashMap<>();
            Map<String, Row> mismatchedRows = new HashMap<>();

            String[] mappings = mapping.split(",");
            Map<String, String> columnMapping = new HashMap<>();
            for (String map : mappings) {
                String[] pair = map.split(":");
                columnMapping.put(pair[0].trim(), pair[1].trim());
            }

            // Store File 1 data in a Map
            for (Row row : sheet1) {
                Cell cell = row.getCell(getColumnIndex(sheet1, primaryKey));
                if (cell != null) {
                    String key = cell.toString();
                    file1Data.put(key, row);
                }
            }

            // Compare File 2 data with File 1 data
            for (Row row : sheet2) {
                Cell cell = row.getCell(getColumnIndex(sheet2, primaryKey));
                if (cell != null) {
                    String key = cell.toString();
                    Row file1Row = file1Data.get(key);
                    if (file1Row != null && compareRows(file1Row, row, columnMapping, sheet1, sheet2)) {
                        matchedRows.put(key, row);
                    } else {
                        mismatchedRows.put(key, row);
                    }
                }
            }

            // Write the results to a new Excel file in the same directory as File 1
            File file11 = new File(file1Path);
            String outputFilePath = file11.getParent() + File.separator + "comparison_result_with_mapping.xlsx";
            Workbook resultWorkbook = new XSSFWorkbook();
            Sheet matchedSheet = resultWorkbook.createSheet("Matched");
            Sheet mismatchedSheet = resultWorkbook.createSheet("Mismatched");

            writeRowsToSheet(matchedRows, matchedSheet);
            writeRowsToSheet(mismatchedRows, mismatchedSheet);

            FileOutputStream outputStream = new FileOutputStream(outputFilePath);
            resultWorkbook.write(outputStream);
            resultWorkbook.close();
            ((Closeable) file11).close();
            file2.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static boolean compareRows(Row row1, Row row2, Map<String, String> columnMapping, Sheet sheet1, Sheet sheet2) {
        for (Map.Entry<String, String> entry : columnMapping.entrySet()) {
            String column1 = entry.getKey();
            String column2 = entry.getValue();
            int colIndex1 = getColumnIndex(sheet1, column1);
            int colIndex2 = getColumnIndex(sheet2, column2);

            Cell cell1 = row1.getCell(colIndex1);
            Cell cell2 = row2.getCell(colIndex2);

            String value1 = cell1 != null ? cell1.toString() : "";
            String value2 = cell2 != null ? cell2.toString() : "";

            if (!value1.equals(value2)) {
                return false;
            }
        }
        return true;
    }

    private static int getColumnIndex(Sheet sheet, String columnName) {
        Row headerRow = sheet.getRow(0);
        for (Cell cell : headerRow) {
            if (cell.getStringCellValue().equals(columnName)) {
                return cell.getColumnIndex();
            }
        }
        return -1;
    }

    private static void writeRowsToSheet(Map<String, Row> rows, Sheet sheet) {
        int rowNum = 0;
        for (Row row : rows.values()) {
            Row newRow = sheet.createRow(rowNum++);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                Cell newCell = newRow.createCell(i);
                Cell oldCell = row.getCell(i);
                if (oldCell != null) {
                    newCell.setCellValue(oldCell.toString());
                }
            }
        }
    }
}
