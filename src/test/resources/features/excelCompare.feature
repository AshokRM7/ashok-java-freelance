Feature: Compare data between two Excel files

  Scenario: Compare Excel files based on primary key and column mappings
    Given I have the Excel files "C:\Users\ADMIN\Downloads\Snowflake_result.xlsx" and "C:\Users\ADMIN\Downloads\Oracle_DB.xlsx"
    #And I have the primary key as "pk"
    And I have the primary key columns "pk" and "pk1"
    And I have the following column mappings:
      | Excel1Column | Excel2Column |
      | Name         | Exp			    |
      | APPROVER_USER_ID         | SERVICE_OFFEREING     |
    When I compare the Excel files
    Then the comparison result should be stored in "C:\Users\ADMIN\Downloads\comparison_result.xlsx"
    

  #Scenario: Compare Excel files with different primary key columns
    #Given I have the Excel files "C:\Users\ADMIN\Downloads\Snowflake_result.xlsx" and "C:\Users\ADMIN\Downloads\Oracle_DB.xlsx"
    #And I have the primary key columns "pk" and "pk"
    #And I have the following column mappings:
      #| Excel1Column | Excel2Column |
      #| Name         | Exp			    |
      #| APPROVER_USER_ID         | SERVICE_OFFEREING     |
    #When I compare the Excel files
    #Then the comparison result should be stored in "comparisonnew_result.xlsx"


