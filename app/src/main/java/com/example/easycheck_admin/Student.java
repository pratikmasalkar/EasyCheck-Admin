package com.example.easycheck_admin;

public class Student {

    private String rollno;
    private String email;
    private String name;
    private String batch; // Update if batch information is not stored within students table
    private String mobile; // Change datatype based on your attendance representation

    public Student() {
    }

    public Student(String rollNo, String name,String email, String batch, String mobile) {
        this.rollno = rollNo;
        this.name = name;
        this.batch = batch;
        this.mobile=mobile;
        this.email=email;
    }

    public String getRollNo() {
        return rollno;
    }

    public void setRollNo(String rollNo) {
        this.rollno = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email){
        this.email=email;
    }
    public String getEmail(){
        return email;
    }

    // You can add additional methods based on your needs

    @Override
    public String toString() {
        return "Student{" +
                "rollNo='" + rollno + '\'' +
                ", name='" + name + '\'' +
                ", batch='" + batch + '\'' +
                ", mobile=" + mobile +
                '}';
    }
}

