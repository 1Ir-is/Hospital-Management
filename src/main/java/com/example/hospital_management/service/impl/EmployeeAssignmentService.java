package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.entity.EmployeeAssignment;
import com.example.hospital_management.entity.ImpatientRecord;
import com.example.hospital_management.repository.IEmployeeAssigmentRepository;
import com.example.hospital_management.repository.IEmployeeRepository;
import com.example.hospital_management.repository.IImpatientRecordRepository;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.hospital_management.service.IEmployeeAssignmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeAssignmentService implements IEmployeeAssignmentService {
    @Autowired
    private IEmployeeAssigmentRepository employeeAssigmentRepository;
    @Autowired
    private IEmployeeRepository employeeRepository;
    @Autowired
    private IImpatientRecordRepository impatientRecordRepository;

    @Override
    public void assignDoctorToRecord(Long recordId, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));
        if (!"Bác sĩ".equals(employee.getPosition().getName())) {
            throw new RuntimeException("Nhân viên không phải là bác sĩ");
        }

        ImpatientRecord record = impatientRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ nội trú"));

        EmployeeAssignment ea = new EmployeeAssignment();
        ea.setEmployee(employee);
        ea.setImpatientRecord(record);
        employeeAssigmentRepository.save(ea);
    }

    @Override
    public void assignNurseToRecord(Long recordId, Long nurseId) {
        Employee nurse = employeeRepository.findById(nurseId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy điều dưỡng"));
        ImpatientRecord record = impatientRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ nội trú")); // 👈 QUAN TRỌNG

        EmployeeAssignment assignment = new EmployeeAssignment();
        assignment.setEmployee(nurse);
        assignment.setImpatientRecord(record); // 👈 set bản ghi đã tồn tại
        employeeAssigmentRepository.save(assignment);
    }

    @Override
    public List<EmployeeAssignment> findAll(Long id) {
        return employeeAssigmentRepository.findByImpatientRecordId(id);
    }

    @Override
    public void save(EmployeeAssignment employeeAssignment) {
        employeeAssigmentRepository.save(employeeAssignment);
    }


    @Override
    public List<EmployeeAssignment> findByInpatientRecordId(Long impatientRecordId) {
        return employeeAssigmentRepository.findByImpatientRecordId(impatientRecordId);
    }

    @Override
    public void deleteByInpatientRecordId(Long inpatientRecordId) {
        employeeAssigmentRepository.deleteByInpatientRecordId(inpatientRecordId);
    }


    @Override
    public Optional<Employee> findDoctorByRecordId(Long recordId) {
        return employeeAssigmentRepository.findLatestDoctorByRecordId(recordId);
    }

    @Override
    public Optional<Employee> findNurseByRecordId(Long recordId) {
        return employeeAssigmentRepository.findLatestNurseByRecordId(recordId);
    }
}
