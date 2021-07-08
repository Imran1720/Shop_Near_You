package com.example.sny;

public class MyModel {
    String url;
    String name,mail,phone,actype,state,district,village,address,id;
    String prul,prname,prmaxcost,prmincost,prcount,prcat,prsdes,prldes,prid;



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

    public String getPrul() {
        return prul;
    }

    public String getPrname() {
        return prname;
    }

    public String getPrmaxcost() {
        return prmaxcost;
    }

    public String getPrmincost() {
        return prmincost;
    }

    public String getPrcount() {
        return prcount;
    }

    public String getPrcat() {
        return prcat;
    }

    public String getPrsdes() {
        return prsdes;
    }

    public String getPrldes() {
        return prldes;
    }

    public String getPrid() {
        return prid;
    }

    //for registering
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

    //for admin to view data
    public MyModel(String url, String name, String mail, String phone, String actype, String id) {
        this.url = url;
        this.name = name;
        this.mail = mail;
        this.phone = phone;
        this.actype = actype;
        this.id = id;
    }

    public MyModel(String prul, String prname, String prmaxcost, String prmincost, String prcount, String prcat, String prsdes, String prldes, String prid) {
        this.prul = prul;
        this.prname = prname;
        this.prmaxcost = prmaxcost;
        this.prmincost = prmincost;
        this.prcount = prcount;
        this.prcat = prcat;
        this.prsdes = prsdes;
        this.prldes = prldes;
        this.prid = prid;
    }

    public MyModel(String prul, String prname, String prmincost, String prsdes, String prid) {
        this.prul = prul;
        this.prname = prname;
        this.prmincost = prmincost;
        this.prsdes = prsdes;
        this.prid = prid;
    }


}
