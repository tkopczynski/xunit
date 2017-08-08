package pl.net.kopczynski.test;

import pl.net.kopczynski.xunit.TestCase;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Tomasz Kopczynski.
 */
public class Tests extends TestCase {

    private Set<String> run = new HashSet<>();
    private int setUpCalls = 0;
    private int tearDownCalls = 0;

    @Override
    public void setUp() {
        setUpCalls++;
    }

    @Override
    public void tearDown() {
        tearDownCalls++;
    }

    public void testEmpty() {
        run.add("testEmpty");
    }

    public void testWithAssertions() {
        run.add("testWithAssertions");
        assertTrue(true);
    }

    public void testFailing() {
        run.add("testFailing");
        assertTrue(false);
    }

    public Set<String> getRun() {
        return run;
    }

    public int getSetUpCalls() {
        return setUpCalls;
    }

    public int getTearDownCalls() {
        return tearDownCalls;
    }
}