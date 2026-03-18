package com.cuoikyweb.cuoi_ky_web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Danh sách department mẫu
    private final List<String> departments = Arrays.asList("Công nghệ thông tin", "Kinh tế", "Ngoại ngữ", "Cơ khí");

    // 1. Hiển thị Form nhập liệu
    @GetMapping
    public String showForm(Model model, HttpSession session) {
        // Nếu user bấm "Back" từ trang confirm, model đã có sẵn đối tượng student do FlashAttribute truyền sang
        if (!model.containsAttribute("student")) {
            Student student = new Student();
            // Lấy department vừa chọn từ Session gán làm mặc định
            String lastDept = (String) session.getAttribute("lastDepartment");
            if (lastDept != null) {
                student.setDepartment(lastDept);
            }
            model.addAttribute("student", student);
        }
        model.addAttribute("departments", departments);
        return "index";
    }

    // 2. Xử lý khi bấm nút Add
    @PostMapping("/add")
    public String processAdd(@Valid @ModelAttribute("student") Student student,
                             BindingResult result,
                             HttpSession session,
                             Model model) {

        // Custom Validate: Kiểm tra trùng ID nếu ID không trống
        if (student.getId() != null && !student.getId().isEmpty()) {
            if (studentService.checkIdExists(student.getId())) {
                result.rejectValue("id", "error.student", "ID này đã tồn tại trong CSDL!");
            }
        }

        // Nếu có lỗi (trống hoặc trùng) -> Trả về lại form
        if (result.hasErrors()) {
            model.addAttribute("departments", departments);
            return "index";
        }

        // Nếu hợp lệ: Lưu tạm vào Session và chuyển hướng đến trang Xác nhận
        session.setAttribute("pendingStudent", student);
        return "redirect:/confirm";
    }

    // 3. Hiển thị trang Xác nhận (Confirm)
    @GetMapping("/confirm")
    public String showConfirmPage(HttpSession session, Model model) {
        Student pendingStudent = (Student) session.getAttribute("pendingStudent");
        if (pendingStudent == null) {
            return "redirect:/"; // Nếu truy cập thẳng không qua form thì đẩy về trang chủ
        }
        model.addAttribute("student", pendingStudent);
        return "confirm";
    }

    // 4. Xử lý khi bấm nút Confirm (Lưu vào DB)
    @PostMapping("/confirm")
    public String saveToDatabase(HttpSession session) {
        Student pendingStudent = (Student) session.getAttribute("pendingStudent");
        if (pendingStudent != null) {
            pendingStudent.setApproved(0); // Set mặc định approved = 0
            studentService.saveStudent(pendingStudent);

            // Lưu lại department vừa chọn vào Session
            session.setAttribute("lastDepartment", pendingStudent.getDepartment());

            // Xóa thông tin tạm
            session.removeAttribute("pendingStudent");
        }
        return "redirect:/";
    }

    // 5. Xử lý khi bấm nút Back (Quay lại form và giữ nguyên dữ liệu vừa nhập)
    @PostMapping("/back")
    public String goBack(HttpSession session, RedirectAttributes redirectAttributes) {
        Student pendingStudent = (Student) session.getAttribute("pendingStudent");
        if (pendingStudent != null) {
            // Dùng Flash Attributes để truyền data về lại file index mà không bị mất
            redirectAttributes.addFlashAttribute("student", pendingStudent);
            session.removeAttribute("pendingStudent");
        }
        return "redirect:/";
    }
}
