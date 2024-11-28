# Future Improvements

This short document should explain shortcut that have been taken during development and how to improve them.

## No Client Validation in Login and Register forms

There is no validation in the forntend if the password has the minimum length and so on.
To fix that use the `zod` library to integrate the validation.
