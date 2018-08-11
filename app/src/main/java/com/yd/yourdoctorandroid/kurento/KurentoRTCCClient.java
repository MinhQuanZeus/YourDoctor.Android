package com.yd.yourdoctorandroid.kurento;

import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.nhancv.webrtcpeer.rtc_peer.RTCClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

import java.net.URISyntaxException;

public class KurentoRTCCClient implements RTCClient {

    private Socket client;
    private String fromPeer, toPeer;
    private boolean isHost;

    public KurentoRTCCClient(Socket client, String fromPeer, String toPeer, boolean isHost) {
        this.fromPeer = fromPeer;
        this.toPeer = toPeer;
        this.client = client;
        this.isHost = isHost;
    }

    @Override
    public void sendOfferSdp(SessionDescription sessionDescription) {

        try {
            if (isHost) {
                JSONObject obj = new JSONObject();
                obj.put("id", "call");
                obj.put("from", fromPeer);
                obj.put("to", toPeer);
                obj.put("sdpOffer", sessionDescription.description);
                client.emit("call", obj);

            } else {
                JSONObject obj = new JSONObject();
                obj.put("id", "incomingCallResponse");
                obj.put("from", fromPeer);
                obj.put("callResponse", "accept");
                obj.put("sdpOffer", sessionDescription.description);
                Log.d("KurentoRTCCCClient", "incomingCallResponse");
                Log.d("KurentoRTCCCClient", obj.toString());
                client.emit("incomingCallResponse", obj);
            }

        } catch (JSONException e) {
            Log.d("sendOfferSdp", e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void sendAnswerSdp(SessionDescription sessionDescription) {

    }

    @Override
    public void sendLocalIceCandidate(IceCandidate iceCandidate) {
        try {

            JSONObject obj = new JSONObject();
            obj.put("id", "onIceCandidate");
            obj.put("userId", fromPeer);
            JSONObject candidate = new JSONObject();
            candidate.put("candidate", iceCandidate.sdp);
            candidate.put("sdpMid", iceCandidate.sdpMid);
            candidate.put("sdpMLineIndex", iceCandidate.sdpMLineIndex);
            obj.put("candidate", candidate);
            Log.e("KurentoRTCCClient", obj.toString());
            client.emit("onIceCandidate",obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendLocalIceCandidateRemovals(IceCandidate[] iceCandidates) {
        Log.e("KurentoRTCCClient", "sendLocalIceCandidateRemovals: ");
    }
}
