package com.lowlevelsubmarine.youtube_api.hooks;

public interface FailHook <T> {
    void onFail(T t);
}
