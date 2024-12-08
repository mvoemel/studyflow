
# Test Cases

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
