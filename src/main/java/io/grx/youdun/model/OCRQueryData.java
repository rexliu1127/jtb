package io.grx.youdun.model;

public class OCRQueryData {

    private String birthday;
    private String id_number;
    private String address;
    private String gender;
    private String nation;
    private String suggest_result;
    private String validity_period;
    private String session_id;
    private String id_name;
    private String idcard_front_photo;
    private String partner_order_id;
    private String thresholds;
    private String result_status;
    private String issuing_authority;
    private String idcard_back_photo;
    private String similarity;
    private String living_photo;
    private String verify_status;
    private String age;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getSuggest_result() {
        return suggest_result;
    }

    public void setSuggest_result(String suggest_result) {
        this.suggest_result = suggest_result;
    }

    public String getValidity_period() {
        return validity_period;
    }

    public void setValidity_period(String validity_period) {
        this.validity_period = validity_period;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getId_name() {
        return id_name;
    }

    public void setId_name(String id_name) {
        this.id_name = id_name;
    }

    public String getIdcard_front_photo() {
        return idcard_front_photo;
    }

    public void setIdcard_front_photo(String idcard_front_photo) {
        this.idcard_front_photo = idcard_front_photo;
    }

    public String getPartner_order_id() {
        return partner_order_id;
    }

    public void setPartner_order_id(String partner_order_id) {
        this.partner_order_id = partner_order_id;
    }

    public String getThresholds() {
        return thresholds;
    }

    public void setThresholds(String thresholds) {
        this.thresholds = thresholds;
    }

    public String getResult_status() {
        return result_status;
    }

    public void setResult_status(String result_status) {
        this.result_status = result_status;
    }

    public String getIssuing_authority() {
        return issuing_authority;
    }

    public void setIssuing_authority(String issuing_authority) {
        this.issuing_authority = issuing_authority;
    }

    public String getIdcard_back_photo() {
        return idcard_back_photo;
    }

    public void setIdcard_back_photo(String idcard_back_photo) {
        this.idcard_back_photo = idcard_back_photo;
    }

    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }

    public String getLiving_photo() {
        return living_photo;
    }

    public void setLiving_photo(String living_photo) {
        this.living_photo = living_photo;
    }

    public String getVerify_status() {
        return verify_status;
    }

    public void setVerify_status(String verify_status) {
        this.verify_status = verify_status;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "OCRQueryData{" +
                "birthday='" + birthday + '\'' +
                ", id_number='" + id_number + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", nation='" + nation + '\'' +
                ", suggest_result='" + suggest_result + '\'' +
                ", validity_period='" + validity_period + '\'' +
                ", session_id='" + session_id + '\'' +
                ", id_name='" + id_name + '\'' +
                ", idcard_front_photo='" + idcard_front_photo + '\'' +
                ", partner_order_id='" + partner_order_id + '\'' +
                ", thresholds='" + thresholds + '\'' +
                ", result_status='" + result_status + '\'' +
                ", issuing_authority='" + issuing_authority + '\'' +
                ", idcard_back_photo='" + idcard_back_photo + '\'' +
                ", similarity='" + similarity + '\'' +
                ", living_photo='" + living_photo + '\'' +
                ", verify_status='" + verify_status + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
