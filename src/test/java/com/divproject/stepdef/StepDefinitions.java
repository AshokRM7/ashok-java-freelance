package com.divproject.stepdef;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class StepDefinitions {

    @Given("I have a configured Cucumber project")
    public void i_have_a_configured_Cucumber_project() {
        System.out.println("Given step executed");
    }

    @When("I run the project")
    public void i_run_the_project() {
        System.out.println("When step executed");
    }

    @Then("I should see the test pass")
    public void i_should_see_the_test_pass() {
        System.out.println("Then step executed");
    }
}

