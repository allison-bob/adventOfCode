# Common Code

Code common to multiple solutions:

* A basis for puzzle solution code to reduce the amount of boilerplate code
  required to get the solution program running
* Data objects utilized in multiple solutions
* Implementations for complex algorithms for easier reuse

The code here is a library used by puzzle solutions. As such, it should be
thoroughly tested:

* Programs using the library should be able to trust that the library works
  correctly; testing of a library should be limited to a "smoke" test to
  validate that required functionality hasn't changed
* Refactoring the library is easier since the testing validates that the
  existing functionality has not been broken
