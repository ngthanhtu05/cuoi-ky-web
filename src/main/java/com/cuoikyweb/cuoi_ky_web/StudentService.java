package com.cuoikyweb.cuoi_ky_web;

public interface StudentService {
    void saveStudent(Student student);
    boolean checkIdExists(String id);
}
