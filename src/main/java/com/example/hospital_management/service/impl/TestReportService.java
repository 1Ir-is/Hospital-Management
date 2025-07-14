package com.example.hospital_management.service.impl;

import com.example.hospital_management.dto.TestRequestDto;
import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.entity.TestReport;
import com.example.hospital_management.entity.TestStatus;
import com.example.hospital_management.repository.ITestReportRepository;
import com.example.hospital_management.repository.ITestStatusRepository;
import com.example.hospital_management.service.ITestReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TestReportService implements ITestReportService {
    private final ITestReportRepository testReportRepository;
    @Autowired
    private ITestStatusRepository testStatusRepository;

    public TestReportService(ITestReportRepository testReportRepository) {
        this.testReportRepository = testReportRepository;
    }

    @Override
    public Page<TestRequestDto> findPendingReportsByRoom(Long roomId, Pageable pageable) {
        return testReportRepository.findPendingReportsByRoom(roomId, pageable);
    }

    @Override
    public void save(TestReport testReport) {
        testReportRepository.save(testReport);
    }

    @Override
    public List<TestReport> findByMedicalRecordId(Long id) {
        return testReportRepository.findByMedicalRecordId(id);
    }

    @Override
    public List<TestReport> findAll() {
        return testReportRepository.findAll();
    }

    @Override
    public Page<TestReport> findAll(Pageable pageable) {
        return testReportRepository.findAll(pageable);
    }

    @Override
    public Optional<TestReport> findById(Long id) {
        return testReportRepository.findById(id);
    }

    @Override
    public void remove(Long id) {
        testReportRepository.deleteById(id);
    }

    @Override
    public Page<TestReport> searchByName(String searchByName, Long medicalRecordId, Pageable pageable) {
        return testReportRepository.searchByName(searchByName, medicalRecordId, pageable);
    }

    @Override
    public List<TestRequestDto> findAllByPatientId(Long patientId) {
        return testReportRepository.findAllByPatientId(patientId);
    }

    @Override
    public List<TestRequestDto> findTodayCompletedReports(Long roomId) {
        return testReportRepository.findTodayCompletedReports(roomId);
    }

    @Transactional
    @Override
    public void updateTestReportStatus(Long testReportId, Long testStatusId) {
        TestReport testReport = testReportRepository.
                findById(testReportId).orElseThrow(() -> new RuntimeException("không tìm thấy test report với id :" + testReportId));
        TestStatus testStatus = testStatusRepository.findById(testStatusId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy test status với ID: " + testStatusId));
        testReport.setTestStatus(testStatus);
        testReport.setDate(LocalDate.now());
        testReportRepository.save(testReport);
    }

    @Override
    public void saveTestResult(Long testReportId, String imageUrl, String note, Long employeeId) {
        TestReport report = testReportRepository.findById(testReportId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu xét nghiệm với ID: " + testReportId));

        report.setImagePath(imageUrl);
        report.setNote(note);
        report.setStatus(true); // đánh dấu đã hoàn tất

        // ✅ Gán lại TestStatus đúng cách (nếu cần)
        TestStatus status = new TestStatus();
        status.setId(2L); // 2: Đã hoàn tất
        report.setTestStatus(status);

        // ✅ Gán lại kỹ thuật viên đúng cách
        Employee technician = new Employee();
        technician.setId(employeeId);
        report.setEmployee(technician);
        testReportRepository.save(report);
    }

    @Override
    public String findImageByTestReportId(Long id) {
        return testReportRepository.findImageByTestReportId(id);
    }
}
