
# Frontend Testing Strategy
Given the extensive number of components and interactions within the frontend, we opted for a manual black box testing strategy. All use cases that were implemented will be tested manually from an end-user perspective.
Manual testing allows us to simulate real user interactions, ensuring that the core functionality and workflows meet the project’s requirements. External people will also be testing our application to ensure unbiased feedback and that it meets usability expectations.
We opted against implementing unit tests for the individual components because of the extensive number of components used in our application, which would have been very time consuming.

To structure our testing process, we have documented key test cases in the following table, outlin-ing the scope, methods, and expected outcomes for each test scenario:


| Test Case Description                     | Objective                                           | Test Data                        | Expected Outcome                                             | Tester      | Status  |
|-------------------------------------------|---------------------------------------------------|----------------------------------|-------------------------------------------------------------|-------------|---------|
| User login (positive case)                | Ensure that valid login credentials work as expected | Valid username and password     | User is successfully logged in and redirected to the dashboard | Team member | Success |
| User login (negative case)                | Verify error handling for invalid login credentials | Invalid username or password    | An error message 'Invalid login credentials' is displayed, and user remains on login page | Team member | Success |
| Create a new study plan (positive case)   | Ensure that study plans are created correctly      | Valid user data (name, goal, parameters) | Study plan is displayed correctly                          | Team member | Success |
| Create a new study plan (negative case)   | Verify error handling for missing or invalid input | Missing or invalid parameters   | An error message 'Invalid input, please check your data' is displayed | Team member | Success |
| Edit an existing study plan (positive case) | Ensure that changes to study plans are saved       | Existing study plan, valid updates | Changes are successfully saved and displayed              | Team member | Success |
| Edit an existing study plan (negative case) | Verify error handling for invalid changes          | Invalid updates (e.g., missing data) | An error message 'Invalid' is displayed                   | Team member | Success |
| Delete a study plan (positive case)       | Ensure study plans are deleted successfully        | Existing study plan             | Study plan is successfully deleted                         | Team member | Success |
| Delete a study plan (negative case)       | Verify behavior when attempting to delete a non-existent plan | Non-existent study plan         | An error message 'Plan not found' is displayed             | Team member | Success |
| Navigation between pages (positive case)  | Ensure navigation works as expected                | Click on valid menu options     | User interface navigates correctly                         | Team member | Success |
| Navigation between pages (negative case)  | Verify behavior for broken links                   | Click on a broken or invalid link | An error message 'Page not found' or a fallback page is displayed | Team member | Success |
| Input field validation (positive case)    | Verify that valid inputs are accepted              | Valid input data                | Data is successfully submitted without error messages      | Team member | Success |
| Input field validation (negative case)    | Verify error handling for empty or invalid inputs  | Empty or invalid input fields   | Proper error messages are displayed, preventing form submission | Team member | Success |


# Backend Testing Strategy
Testing for StudyFlow focuses primarily on unit tests to check the core functions of the application for stability and reliability. Unit tests help to detect errors early and make the code more maintain-able overall. Each test isolates a specific function or method to ensure that it delivers the ex-pected results.

The aim of unit tests:
Unit tests are designed to ensure that individual code units such as methods or functions work correctly independently. The main goal is to identify errors at an early stage and thus increase the quality and reliability of the application.

Testing environment and tools:
JUnit is used as a framework for the Java-based backend components.
Mockito is used to mock external dependencies so that functions can be tested in isolation.

Some key test cases for StudyFlow include:
•	Learning plan generation: tests whether a correctly calculated schedule is created based on user input.
•	User registration: Checks the validation of input fields, such as email format and password security.
•	Calendar entries: Checks that appointments are added correctly and included in the learn-ing schedule.
