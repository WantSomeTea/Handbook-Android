package com.sergey.handbook;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sergey.
 */

public class Employee implements Parcelable {
    private String name;
    private String phoneNumber;
    private String workNumber;
    private String email;
    private String companyName;
    private String jobName;
    private String departmentName;

    public static final Creator<Employee> CREATOR = new Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel in) {
            Employee employee = new Employee();
            employee.name = in.readString();
            employee.phoneNumber = in.readString();
            employee.workNumber = in.readString();
            employee.email = in.readString();
            employee.companyName = in.readString();
            employee.jobName = in.readString();
            employee.departmentName = in.readString();
            return employee;
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phoneNumber);
        dest.writeString(workNumber);
        dest.writeString(email);
        dest.writeString(companyName);
        dest.writeString(jobName);
        dest.writeString(departmentName);
    }
}
