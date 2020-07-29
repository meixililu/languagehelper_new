package com.messi.languagehelper;

import android.content.pm.PackageInfo;
import android.test.ApplicationTestCase;
import android.test.MoreAsserts;

import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.StringUtils;

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
        application = getApplication();                    //0 10 164a s dfaa dsfadsfd sf2dwf dwd
        LogUtil.DefalutLog(StringUtils.replaceAll("0 1:0 1.64,a s dfa.a dsfa?dsfd sf2,dw.f :'dwd."));
        LogUtil.DefalutLog(StringUtils.replaceSome("0 1:0 1.64,a s dfa.a dsfa?dsfd sf2,dw.f :'dwd."));
    }

    public void testCorrectVersion() throws Exception {
        PackageInfo info = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
        System.out.println(info.packageName);
        System.out.println(info.versionName);
        assertNotNull(info);
        MoreAsserts.assertMatchesRegex("\\d\\.\\d.\\d", info.versionName);
    }

}