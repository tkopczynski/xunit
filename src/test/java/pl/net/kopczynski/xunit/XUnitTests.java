package pl.net.kopczynski.xunit;

import org.junit.Before;
import org.junit.Test;
import pl.net.kopczynski.test.EmptyTests;
import pl.net.kopczynski.test.Tests;
import pl.net.kopczynski.xunit.exception.FailedTestsException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by Tomasz Kopczynski.
 */
public class XUnitTests {

    private TestRunner testRunner = new TestRunner();
    private Tests tests;

    @Before
    public void init() {
        tests = new Tests();
    }

    @Test
    public void should_run_a_test_with_assertions() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        testRunner.run(tests, getTestMethod("testWithAssertions"));
        assertThat(tests.getRun()).hasSize(1);
        assertThat(tests.getRun()).contains("testWithAssertions");
    }

    @Test
    public void should_run_a_failing_test() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertThatThrownBy(() -> testRunner.run(tests, getTestMethod("testFailing"))).isInstanceOf(FailedTestsException.class);
        assertThat(tests.getRun()).hasSize(1);
        assertThat(tests.getRun()).contains("testFailing");
    }

    @Test
    public void should_call_set_up_method_before_test_and_tear_down_after_test() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        testRunner.run(tests, getTestMethod("testEmpty"));
        assertThat(tests.getRun()).hasSize(1);
        assertThat(tests.getRun()).contains("testEmpty");
        assertThat(tests.getSetUpCalls()).isEqualTo(1);
        assertThat(tests.getTearDownCalls()).isEqualTo(1);
    }

    @Test
    public void should_call_tear_down_after_failing_test() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        try {
            testRunner.run(tests, getTestMethod("testFailing"));
        } catch (FailedTestsException e) {
        }

        assertThat(tests.getRun()).hasSize(1);
        assertThat(tests.getRun()).contains("testFailing");
        assertThat(tests.getTearDownCalls()).isEqualTo(1);
    }

    @Test
    public void should_run_many_passing_test_methods_in_class() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        testRunner.run(tests, getTestMethod("testEmpty"), getTestMethod("testWithAssertions"));
        assertThat(tests.getRun()).hasSize(2);
        assertThat(tests.getRun()).contains("testEmpty", "testWithAssertions");
        assertThat(tests.getTearDownCalls()).isEqualTo(2);
        assertThat(tests.getSetUpCalls()).isEqualTo(2);
    }

    @Test
    public void should_run_passing_and_failing_methods_in_class() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertThatThrownBy(() -> testRunner.run(tests, getTestMethod("testEmpty"), getTestMethod("testFailing"), getTestMethod("testWithAssertions"))).isInstanceOf(FailedTestsException.class);
        assertThat(tests.getRun()).hasSize(3);
        assertThat(tests.getRun()).contains("testEmpty", "testWithAssertions", "testFailing");
        assertThat(tests.getTearDownCalls()).isEqualTo(3);
        assertThat(tests.getSetUpCalls()).isEqualTo(3);
    }

    @Test
    public void shoud_collect_failed_tests() {
        Throwable exception = catchThrowable(() -> testRunner.run(tests, getTestMethod("testEmpty"), getTestMethod("testFailing"), getTestMethod("testWithAssertions")));
        assertThat(exception).isInstanceOf(FailedTestsException.class);
        assertThat(((FailedTestsException) exception).getFailedTests()).contains("testFailing");
    }

    @Test
    public void should_run_test_without_set_up_and_tear_down_methods() {
        EmptyTests emptyTests = new EmptyTests();
        assertThatCode(() -> testRunner.run(emptyTests, emptyTests.getClass().getDeclaredMethod("testEmpty"))).doesNotThrowAnyException();
    }

    private Method getTestMethod(String methodName) throws NoSuchMethodException {
        return tests.getClass().getDeclaredMethod(methodName);
    }
}
