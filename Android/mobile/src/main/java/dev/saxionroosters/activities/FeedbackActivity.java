package dev.saxionroosters.activities;

import com.heinrichreimersoftware.androidissuereporter.IssueReporterActivity;
import com.heinrichreimersoftware.androidissuereporter.model.github.ExtraInfo;
import com.heinrichreimersoftware.androidissuereporter.model.github.GithubTarget;

/**
 * Created by Jelle on 21-8-2016.
 */
public class FeedbackActivity extends IssueReporterActivity {

    // Where should the issues go?
    // (http://github.com/username/repository)
    @Override
    protected GithubTarget getTarget() {
        return new GithubTarget("doppie", "SaxionRoosters");
    }

    // [Optional] Auth token to open issues if users don't have a GitHub account
    // You can register a bot account on GitHub and copy ist OAuth2 token here.
    @Override
    public String getGuestToken() {
        return "";
    }

    // [Optional] Include other relevant info in the bug report (like custom variables)
//    @Override
//    public void onSaveExtraInfo(ExtraInfo extraInfo) {
//        extraInfo.put("Test 1", "Example string");
//        extraInfo.put("Test 2", true);
//    }
}
