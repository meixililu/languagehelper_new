package com.messi.languagehelper;

import com.messi.languagehelper.util.KStringUtils;

import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
//@RunWith(AndroidJUnit4.class)
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        System.out.println(KStringUtils.INSTANCE.getTimeMills("01:01.64"));
    }
}