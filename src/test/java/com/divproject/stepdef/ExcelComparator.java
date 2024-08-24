package com.divproject.stepdef;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ExcelComparator {

    public static void main(String[] args) throws IOException {
        String excelFilePath1 = "path_to_excel1.xlsx";
        String excelFilePath2 = "path_to_excel2.xlsx";
        String resultFilePath = "comparison_result.xlsx";

        // Column mappings between Excel1 and Excel2
        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("Age", "Experience");
        columnMapping.put("Name", "FullName");
        // Add more mappings as needed

        // Primary Key Column
        String primaryKey = "ID";

//        compareExcelFiles(excelFilePath1, excelFilePath2, primaryKey, primaryKey2, columnMapping, resultFilePath);
    }

    public static void compareExcelFiles(String filePath1, String filePath2, String primaryKey, String primaryKey2,
                                         Map<String, String> columnMapping, String resultFilePath) throws IOException {

        FileInputStream fis1 = new FileInputStream(filePath1);
        FileInputStream fis2 = new FileInputStream(filePath2);
        Workbook workbook1 = new XSSFWorkbook(fis1);
        Workbook workbook2 = new XSSFWorkbook(fis2);
        Sheet sheet1 = workbook1.getSheetAt(0);
        Sheet sheet2 = workbook2.getSheetAt(0);

        Map<String, Row> excel1Data = loadData(sheet1, primaryKey);
        Map<String, Row> excel2Data = loadData(sheet2, primaryKey2);

        Workbook resultWorkbook = new XSSFWorkbook();
        Sheet resultSheet = resultWorkbook.createSheet("Comparison Result");

        // Write header
        Row headerRow = resultSheet.createRow(0);
        int cellIndex = 0;
        headerRow.createCell(cellIndex++).setCellValue(primaryKey);
        for (String col1 : columnMapping.keySet()) {
            headerRow.createCell(cellIndex++).setCellValue(col1 + " (Excel1)");
            headerRow.createCell(cellIndex++).setCellValue(columnMapping.get(col1) + " (Excel2)");
            headerRow.createCell(cellIndex++).setCellValue("Match?");
        }

        int rowIndex = 1;
        for (String key : excel1Data.keySet()) {
            Row resultRow = resultSheet.createRow(rowIndex++);
            resultRow.createCell(0).setCellValue(key);
            Row row1 = excel1Data.get(key);
            Row row2 = excel2Data.get(key);

            cellIndex = 1;
            for (Map.Entry<String, String> entry : columnMapping.entrySet()) {
                String col1 = entry.getKey();
                String col2 = entry.getValue();

                Cell cell1 = row1.getCell(getColumnIndex(sheet1, col1));
                Cell cell2 = row2 != null ? row2.getCell(getColumnIndex(sheet2, col2)) : null;

                String value1 = getCellValue(cell1);
                String value2 = getCellValue(cell2);

                resultRow.createCell(cellIndex++).setCellValue(value1);
                resultRow.createCell(cellIndex++).setCellValue(value2);

                boolean match = value1.equals(value2);
                resultRow.createCell(cellIndex++).setCellValue(match ? "Yes" : "No");
            }
        }

        try (FileOutputStream fos = new FileOutputStream(resultFilePath)) {
            resultWorkbook.write(fos);
        }

        workbook1.close();
        workbook2.close();
        resultWorkbook.close();
    }

    private static Map<String, Row> loadData(Sheet sheet, String primaryKey) {
        Map<String, Row> dataMap = new HashMap<>();
        int primaryKeyIndex = getColumnIndex(sheet, primaryKey);

        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next(); // Skip header

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell primaryKeyCell = row.getCell(primaryKeyIndex);
            String key = getCellValue(primaryKeyCell);
            dataMap.put(key, row);
        }
        return dataMap;
    }

    private static int getColumnIndex(Sheet sheet, String columnName) {
        Row headerRow = sheet.getRow(0);
        for (Cell cell : headerRow) {
            if (cell.getStringCellValue().equalsIgnoreCase(columnName)) {
                return cell.getColumnIndex();
            }
        }
        return -1; // Column not found
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}

