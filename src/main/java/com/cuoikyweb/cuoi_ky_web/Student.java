package com.cuoikyweb.cuoi_ky_web;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "Student")
public class Student {

    @Id
    @NotBlank(message = "ID không được để trống")
    private String id;

    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotNull(message = "Ngày sinh không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @NotBlank(message = "Phòng ban không được để trống")
    private String department;

    // Giá trị mặc định là 0 theo yêu cầu
    private Integer approved = 0;

    // Getters and Setters (Nếu dùng thư viện Lombok thì chỉ cần thêm @Data ở đầu class)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Integer getApproved() { return approved; }
    public void setApproved(Integer approved) { this.approved = approved; }
}