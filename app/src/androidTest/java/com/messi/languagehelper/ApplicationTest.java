package com.messi.languagehelper;

import android.content.pm.PackageInfo;
import android.test.ApplicationTestCase;
import android.test.MoreAsserts;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<BaseApplication> {

    private BaseApplication application;

    public ApplicationTest() {
        super(BaseApplication.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        application = getApplication();
    }

    public void testCorrectVersion() throws Exception {
        PackageInfo info = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
        System.out.println(info.packageName);
        System.out.println(info.versionName);
        assertNotNull(info);
        MoreAsserts.assertMatchesRegex("\\d\\.\\d.\\d", info.versionName);
    }

}