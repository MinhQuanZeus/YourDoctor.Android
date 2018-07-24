package com.yd.yourdoctorandroid.networks.getListRecommentDoctor;

import java.util.List;

public class MainObjectRecommend {
    private List<DoctorRecommend> listDoctor;

    public MainObjectRecommend(List<DoctorRecommend> listDoctor) {
        this.listDoctor = listDoctor;
    }

    public List<DoctorRecommend> getListDoctor() {
        return listDoctor;
    }

    public void setListDoctor(List<DoctorRecommend> listDoctor) {
        this.listDoctor = listDoctor;
    }
}
