package com.cecs544.BugReporter.enums;

import com.cecs544.BugReporter.util.Constants;

public enum Resolution {
    PENDING(Constants.PENDING),
    FIXED(Constants.FIXED),
    IRREPRODUCIBLE(Constants.IRREPRODUCIBLE),
    DEFERRED(Constants.DEFERRED),
    AS_DESIGNED(Constants.AS_DESIGNED),
    CANNOTFIX(Constants.CANNOTFIX),
    WITHDRAWN_BY_REPORTER(Constants.WITHDRAWN_BY_REPORTER),
    NEED_MORE_INFO(Constants.NEED_MORE_INFO),
    DISAGREE_WITH_SUGGESTION(Constants.DISAGREE_WITH_SUGGESTION);

    public final String resolution;

    Resolution(String resolution) {
        this.resolution = resolution;
    }

    public String getResolution() {
        return resolution;
    }
}
