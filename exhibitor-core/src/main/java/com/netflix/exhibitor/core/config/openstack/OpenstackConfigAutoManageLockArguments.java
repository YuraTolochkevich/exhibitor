package com.netflix.exhibitor.core.config.openstack;

import com.netflix.exhibitor.core.config.AutoManageLockArguments;

import java.util.concurrent.TimeUnit;


public class OpenstackConfigAutoManageLockArguments extends AutoManageLockArguments {
        private final int settlingMs;

        public OpenstackConfigAutoManageLockArguments(String prefix) {
        super(prefix);
        settlingMs = (int) TimeUnit.SECONDS.toMillis(5);
    }

        public OpenstackConfigAutoManageLockArguments(String prefix, int timeoutMs, int pollingMs, int settlingMs) {
        super(prefix, timeoutMs, pollingMs);
        this.settlingMs = settlingMs;
    }

    public int getSettlingMs() {
        return settlingMs;
    }
}

