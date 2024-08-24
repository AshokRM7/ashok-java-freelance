package com.divproject.stepdef;

import io.cucumber.java.en.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExcelComparisonSteps {

    private String excelFilePath1;
    private String excelFilePath2;
    private String primaryKey;
    private String primaryKey2;
    private Map<String, String> columnMapping = new HashMap<>();
    private String resultFilePath;

    @Given("I have the Excel files {string} and {string}")
    public void iHaveTheExcelFilesAnd(String file1, String file2) {
        this.excelFilePath1 = file1;
        this.excelFilePath2 = file2;
    }

//    @Given("I have the primary key as {string}")
    @Given("I have the primary key columns {string} and {string}")
    public void iHaveThePrimaryKeyAs(String primaryKey, String primaryKey2) {
        this.primaryKey = primaryKey;
        this.primaryKey2 = primaryKey2;
    }

    @Given("I have the following column mappings:")
    public void iHaveTheFollowingColumnMappings(io.cucumber.datatable.DataTable dataTable) {
        dataTable.asMaps().forEach(row -> {
            String excel1Column = row.get("Excel1Column");
            String excel2Column = row.get("Excel2Column");
            columnMapping.put(excel1Column, excel2Column);
        });
    }

    @When("I compare the Excel files")
    public void iCompareTheExcelFiles() throws IOException {
        this.resultFilePath = "C:\\Users\\ADMIN\\Downloads\\comparisonlae_result.xlsx"; // Set the output file path
        ExcelComparator.compareExcelFiles(excelFilePath1, excelFilePath2, primaryKey, primaryKey2, columnMapping, resultFilePath);
    }

    @Then("the comparison result should be stored in {string}")
    public void theComparisonResultShouldBeStoredIn(String resultFilePath) {
        // You can add any additional verification if needed
        System.out.println("Comparison completed. Result stored in: " + resultFilePath);
    }
}
