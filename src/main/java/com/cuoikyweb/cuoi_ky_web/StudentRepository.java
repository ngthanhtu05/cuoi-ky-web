package com.cuoikyweb.cuoi_ky_web;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    // Kế thừa sẵn save(), findById()...
    // Khai báo thêm hàm kiểm tra trùng ID
    boolean existsById(String id);
}
