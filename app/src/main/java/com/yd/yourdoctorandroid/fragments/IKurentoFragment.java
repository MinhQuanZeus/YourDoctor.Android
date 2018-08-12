package com.yd.yourdoctorandroid.fragments;

import org.webrtc.EglBase;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoRenderer;

public interface IKurentoFragment {
    void logAndToast(String msg);

    void disconnect();

    VideoCapturer createVideoCapturer();

    EglBase.Context getEglBaseContext();

    VideoRenderer.Callbacks getLocalProxyRenderer();

    VideoRenderer.Callbacks getRemoteProxyRenderer();

    void setSwappedFeeds(boolean swappedFeed);

    void socketConnect(boolean success);

    void registerStatus(boolean success);

    void transactionToCalling(String fromPeer, String toPeer, boolean isHost);

    void incomingCalling(String fromPeer);

    void stopCalling();

    void startCallIng();
}
