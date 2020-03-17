package com.bearing_price.data;


public interface Webservice {


    void check_version(final VersionDto versionDto, final OnFinishedListener finishedListener);

    interface OnFinishedListener {
        void onFinished(Object responseDto);

        void onFailure(Throwable t);
    }
}
