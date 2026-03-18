package com.cuoikyweb.cuoi_ky_web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    @Override
    public boolean checkIdExists(String id) {
        return studentRepository.existsById(id);
    }
}
