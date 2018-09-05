package com.yd.yourdoctorandroid.networks.getHistoryVideoCall;


import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.DoctorID;

public class ObjectHistoryVideo {
    private String _id;
    private long timeStart;
    private long timeEnd;
    private String linkVideo;
    private DoctorID doctorId;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getLinkVideo() {
        return linkVideo;
    }

    public void setLinkVideo(String linkVideo) {
        this.linkVideo = linkVideo;
    }

    public DoctorID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(DoctorID doctorId) {
        this.doctorId = doctorId;
    }
}
