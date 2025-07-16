package com.stanzaliving.laundry.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

public final class Constants {
    public static final String COLLECTION = "collection";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Bean {
        public static final String SCHEDULED_THREAD_POOL = "scheduledThreadPool";
    }
}
