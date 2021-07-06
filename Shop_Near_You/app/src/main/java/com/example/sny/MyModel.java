package com.example.sny;

public class MyModel {
    String url;
    String name,mail,phone,actype,state,district,village,address,id;
    String pname,pmax,pmin,pod,pld,prurl,pid;

    public MyModel(String pid) {
        this.pid = pid;
    }

    public MyModel(String pname, String pmin, String pod, String prurl, String pid) {
        this.pname = pname;
        this.pmin = pmin;
        this.pod = pod;
        this.prurl = prurl;
        this.pid = pid;
    }

    public String getPid() {
        return pid;
    }

    public MyModel(String pname, String pmax, String pmin, String pod, String pld, String prurl, String pid) {
        this.pname = pname;
        this.pmax = pmax;
        this.pmin = pmin;
        this.pod = pod;
        this.pld = pld;
        this.prurl = prurl;
        this.pid = pid;
    }

    public String getPrurl() {
        return prurl;
    }
    public String getPname() {
        return pname;
    }

    public String getPmax() {
        return pmax;
    }

    public String getPmin() {
        return pmin;
    }

    public String getPod() {
        return pod;
    }

    public String getPld() {
        return pld;
    }

    public MyModel(String url, String name, String mail, String phone, String actype, String id) {
        this.url = url;
        this.name = name;
        this.mail = mail;
        this.phone = phone;
        this.actype = actype;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getPhone() {
        return phone;
    }

    public String getActype() {
        return actype;
    }

    public String getState() {
        return state;
    }

    public String getDistrict() {
        return district;
    }

    public String getVillage() {
        return village;
    }

    public String getAddress() {
        return address;
    }

    public MyModel(String url, String name, String mail, String phone, String actype, String state, String district, String village, String address, String id) {
        this.url = url;
        this.name = name;
        this.mail = mail;
        this.phone = phone;
        this.actype = actype;
        this.state = state;
        this.district = district;
        this.village = village;
        this.address = address;
        this.id = id;
    }



}
