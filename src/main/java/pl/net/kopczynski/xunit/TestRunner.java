package pl.net.kopczynski.xunit;

import pl.net.kopczynski.xunit.exception.FailedTestsException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Tomasz Kopczynski.
 */
public class TestRunner {

    public void run(TestCase tests, Method... methods) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method setUpMethod = tests.getClass().getMethod("setUp");
        Method tearDownMethod = tests.getClass().getMethod("tearDown");
        Set<String> failedTests = new HashSet<>();
        for (Method method : methods) {
            setUpMethod.invoke(tests);
            try {
                method.invoke(tests);
            } catch (InvocationTargetException ex) {
                failedTests.add(method.getName());
            } finally {
                tearDownMethod.invoke(tests);
            }
        }
        if (!failedTests.isEmpty()) {
            throw new FailedTestsException(failedTests);
        }
    }
}
