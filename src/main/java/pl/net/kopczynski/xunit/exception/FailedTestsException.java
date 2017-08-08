package pl.net.kopczynski.xunit.exception;

import java.util.Set;

/**
 * Created by Tomasz Kopczynski.
 */
public class FailedTestsException extends RuntimeException {
    private Set<String> failedTests;

    public FailedTestsException(Set<String> failedTests) {
        super();
        this.failedTests = failedTests;
    }

    public Set<String> getFailedTests() {
        return this.failedTests;
    }
}
