package com.example.hospital_management.service.impl;

import com.example.hospital_management.dto.BillingSummaryDto;
import com.example.hospital_management.dto.MedicalRecordBasicDto;
import com.example.hospital_management.dto.MedicalRecordDto;
import com.example.hospital_management.dto.TestSummaryDto;
import com.example.hospital_management.entity.ExaminationShift;
import com.example.hospital_management.entity.MedicalRecord;
import com.example.hospital_management.repository.IMedicalRecordRepository;
import com.example.hospital_management.repository.IPrescriptionRepository;
import com.example.hospital_management.repository.ITestReportRepository;
import com.example.hospital_management.service.IExaminationShiftService;
import com.example.hospital_management.service.IExaminationShiftStatusService;
import com.example.hospital_management.service.IInsuranceService;
import com.example.hospital_management.service.IInsuranceService;
import com.example.hospital_management.service.IMedicalRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MedicalRecordService implements IMedicalRecordService {
    private final IMedicalRecordRepository medicalRecordRepository;

    private final ITestReportRepository testReportRepository;

    private final IPrescriptionRepository prescriptionRepository;
    private final IExaminationShiftService examinationShiftService;
    private final IExaminationShiftStatusService examinationShiftStatusService;
    private final IInsuranceService iInsuranceService;

    public MedicalRecordService(IMedicalRecordRepository medicalRecordRepository, ITestReportRepository testReportRepository,
                                IPrescriptionRepository prescriptionRepository, IExaminationShiftService examinationShiftService, IExaminationShiftStatusService examinationShiftStatusService, IInsuranceService iInsuranceService) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.testReportRepository = testReportRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.examinationShiftService = examinationShiftService;
        this.examinationShiftStatusService = examinationShiftStatusService;
        this.iInsuranceService = iInsuranceService;
    }

    @Override
    public BillingSummaryDto getBillingSummary(Long medicalRecordId) {
        MedicalRecord mr = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ khám"));

        // Lấy phí các phần
        Long medicineFee = medicalRecordRepository.getTotalMedicineFee(medicalRecordId);
        Long testFee = medicalRecordRepository.getTotalTestFee(medicalRecordId);
        Long advance = medicalRecordRepository.getAdvancePayment(medicalRecordId);

        // Gán mặc định nếu null
        medicineFee = (medicineFee == null) ? 0 : medicineFee;
        testFee = (testFee == null) ? 0 : testFee;
        advance = (advance == null) ? 0 : advance;

        Long medicalFee = (mr.getFee() == null) ? 0 : mr.getFee();
        Long totalFee = medicalFee + testFee + medicineFee;

        // Tính bảo hiểm (giảm 50% nếu có thẻ còn hiệu lực)
        Long insuranceAmount = 0L;
        Long patientId = mr.getPatient().getId();
        if (iInsuranceService.existsValidInsurance(patientId, LocalDate.now())) {
            insuranceAmount = totalFee * 30 / 100;
        }

        // Tính số tiền còn lại phải thanh toán
        Long remaining = totalFee - insuranceAmount - advance;

        // Trả về DTO
        return new BillingSummaryDto(
                mr.getId(),
                mr.getPatient().getName(),
                mr.getCode(),
                medicalFee,
                testFee,
                medicineFee,
                totalFee,
                insuranceAmount,    // ✅ Trường mới thêm
                advance,
                remaining,
                false // có thể dùng mr.getInpatient() nếu bạn có cờ phân biệt nội trú
        );
    }

    @Override
    public Page<MedicalRecordBasicDto> findAllBasicInfo(Pageable pageable) {
        return medicalRecordRepository.findAllBasicInfo(pageable);
    }

    @Override
    public void markAsPaid(Long medicalRecordId) {
        // ✅ Cập nhật trạng thái hồ sơ khám
        medicalRecordRepository.updatePaymentStatus(medicalRecordId);

        // ✅ Cập nhật trạng thái xét nghiệm
        testReportRepository.markTestsAsPaidByMedicalRecord(medicalRecordId);

        // ✅ Cập nhật trạng thái đơn thuốc
        prescriptionRepository.markPrescriptionsAsPaidByMedicalRecord(medicalRecordId);
    }


//    private final IMedicalRecordRepository medicalRecordRepository;

//    @Autowired
//    public MedicalRecordService(IMedicalRecordRepository medicalRecordRepository) {
//        this.medicalRecordRepository = medicalRecordRepository;
//    }

    @Override
    public void save(MedicalRecord medicalRecord) {
        medicalRecordRepository.save(medicalRecord);
    }

    @Override
    public Long findMaxId() {
        return medicalRecordRepository.findMaxId();
    }

    @Override
    public Page<MedicalRecord> findAllWithOutVitalSign(Pageable pageable) {
        return medicalRecordRepository.findAllWithOutVitalSign(pageable);
    }

    @Override
    public MedicalRecord findById(Long id) {
        return medicalRecordRepository.findById(id).orElse(null);
    }

    @Override
    public Page<MedicalRecordDto> getWaitingRecords(Pageable pageable) {
        return medicalRecordRepository.getAllStatusRecords(pageable);
    }

    @Override
    public MedicalRecordDto getCurrentPatient() {
        return medicalRecordRepository.getCurrentRecord();
    }

//    @Override
//    public Optional<MedicalRecord> findById(Long id) {
//        return medicalRecordRepository.findById(id);
//    }

    @Override
    public Page<TestSummaryDto> getTestingMedicalRecordList(Pageable pageable, Long roomId) {
        Page<TestSummaryDto> testSummaryDtos =  medicalRecordRepository.getTestingMedicalRecordList(pageable, roomId);
        if(testSummaryDtos.isEmpty()){
            return testSummaryDtos;
        }
        for (TestSummaryDto dto : testSummaryDtos){
            if(dto.getCompletedTest().equals(dto.getTotalOfTest())){
                ExaminationShift shift = examinationShiftService.findByMedicalRecordId(dto.getMedicalRecordId());
                if(shift != null && shift.getExaminationShiftStatus().getId() != 4L){
                    shift.setExaminationShiftStatus(examinationShiftStatusService.findById(4L));
                    examinationShiftService.save(shift);
                }
            }

        }
        return testSummaryDtos;
    }

    @Override
    public MedicalRecordDto getCurrentPatient(Long roomId) {
        return medicalRecordRepository.getCurrentRecord(roomId);
    }

    @Override
    public Page<MedicalRecordDto> getWaitingRecords(Pageable pageable, Long roomId) {
        return medicalRecordRepository.getWaitingRecords(pageable, roomId);
    }

    @Override
    public MedicalRecord getMedicalRecordById(Long id) {
        return medicalRecordRepository.getMedicalRecordById(id);
    }

    @Override
    public Page<MedicalRecordDto> getAllStatusRecords(Pageable pageable, Long roomId) {
        return medicalRecordRepository.getAllStatusRecords(pageable, roomId);
    }

    @Override
    public List<MedicalRecord> findByPatientIdCard(String idCard) {
        return medicalRecordRepository.findByPatientIdCard(idCard);
    }

    @Override
    public List<MedicalRecord> findAll() {
        return medicalRecordRepository.findAll();
    }

    public List<BillingSummaryDto> getBillingSummaryToday() {
        List<Object[]> rawList = medicalRecordRepository.findAllPaidTodayNative();
        return rawList.stream().map(r -> new BillingSummaryDto(
                ((Number) r[0]).longValue(),        // medicalRecordId
                (String) r[1],                      // patientName
                (String) r[2],                      // medicalRecordCode
                toLong(r[3]),                       // medicalFee
                toLong(r[4]),                       // medicineFee
                toLong(r[5]),                       // testFee
                toLong(r[6]),                       // advancePayment
                toLong(r[7]),                       // insuranceAmount
                toLong(r[8]),                       // totalFee
                toLong(r[9]),                       // remainingAmount
                toBoolean(r[10])                    // inpatient
        )).toList();
    }

    private Boolean toBoolean(Object o) {
        if (o == null) return false;
        if (o instanceof Boolean) return (Boolean) o;
        if (o instanceof Number) return ((Number) o).intValue() != 0;
        return Boolean.parseBoolean(o.toString());
    }

    private Long toLong(Object o) {
        return o == null ? 0L : ((Number) o).longValue();
    }
}
