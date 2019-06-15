package com.lowlevelsubmarine.youtube_api.hooks;

public interface SuccessHook<T> {
    void onSuccess(T t);
}
