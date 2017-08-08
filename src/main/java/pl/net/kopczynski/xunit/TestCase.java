package pl.net.kopczynski.xunit;

import pl.net.kopczynski.xunit.exception.AssertionException;

/**
 * Created by Tomasz Kopczynski.
 */
public abstract class TestCase {
    public void assertTrue(boolean bool) {
        if (!bool) {
            throw new AssertionException();
        }
    }

    public void setUp() {

    }

    public void tearDown() {

    }

}
