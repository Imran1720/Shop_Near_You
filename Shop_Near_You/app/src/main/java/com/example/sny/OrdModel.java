package com.example.sny;

public class OrdModel {

    String purl,pname,psdes,pcost,pid,sid,stat;

    public String getStat() {
        return stat;
    }

    public String getPurl() {
        return purl;
    }

    public String getPname() {
        return pname;
    }

    public String getPsdes() {
        return psdes;
    }

    public String getPcost() {
        return pcost;
    }

    public String getPid() {
        return pid;
    }

    public String getSid() {
        return sid;
    }


    public OrdModel(String purl, String pname, String psdes, String pcost, String pid, String sid,String stat) {
        this.purl = purl;
        this.pname = pname;
        this.psdes = psdes;
        this.pcost = pcost;
        this.pid = pid;
        this.sid = sid;
        this.stat = stat;
    }
}
