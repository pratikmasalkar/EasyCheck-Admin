package com.example.easycheck_admin;

public class Teacher {

    private String name;
    private String email;
    private String course; // Update if batch information is not stored within students table
    private String mobile; // Change datatype based on your attendance representation

    public Teacher() {
    }

    public Teacher( String name,String email, String course, String mobile) {
        this.name = name;
        this.course = course;
        this.mobile=mobile;
        this.email=email;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
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
        return "Teacher{" +
                ", name='" + name + '\'' +
                ", course='" + course + '\'' +
                ", mobile=" + mobile +
                ", email=" + email +
                '}';
    }
}

