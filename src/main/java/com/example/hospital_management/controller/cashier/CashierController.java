package com.example.hospital_management.controller.cashier;

import com.example.hospital_management.dto.BillingSummaryDto;
import com.example.hospital_management.dto.ImpatientBasicDto;
import com.example.hospital_management.dto.MedicalRecordBasicDto;
import com.example.hospital_management.service.IImpatientRecordService;
import com.example.hospital_management.service.IMedicalRecordService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/cashier")
public class CashierController {

    private final IMedicalRecordService medicalRecordService;
    private final IImpatientRecordService impatientRecordService;

    public CashierController(IMedicalRecordService medicalRecordService, IImpatientRecordService impatientRecordService) {
        this.medicalRecordService = medicalRecordService;
        this.impatientRecordService = impatientRecordService;
    }

    // ✅ Hiển thị dashboard mặc định là ngoại trú
    @GetMapping("")
    public String listRecords(@RequestParam(value = "type", defaultValue = "out") String type, Model model) {
        model.addAttribute("type", type);

        if (type.equals("in")) {
            List<ImpatientBasicDto> records = impatientRecordService.findAllUnpaidImpatients();
            model.addAttribute("records", records);
        } else {
            List<MedicalRecordBasicDto> records = medicalRecordService.findAllBasicInfo();
            model.addAttribute("records", records);
        }
        return "cashier/dashboard";
    }

    @GetMapping("/detail-fragment")
    public String getSummaryFragment(@RequestParam("id") Long id, Model model) {
        BillingSummaryDto summary = medicalRecordService.getBillingSummary(id);
        model.addAttribute("summary", summary);
        return "cashier/summary-fragment :: summary";
    }

    @GetMapping("/inpatient/detail-fragment")
    public String getInpatientSummaryFragment(@RequestParam("id") Long id, Model model) {
        BillingSummaryDto summary = impatientRecordService.getBillingSummary(id);
        model.addAttribute("summary", summary);
        return "cashier/summary-fragment :: summary";
    }

    @PostMapping("/pay")
    public void payAndExportPdf(@RequestParam("id") Long medicalRecordId,
                                HttpServletResponse response) throws IOException, DocumentException {


        medicalRecordService.markAsPaid(medicalRecordId);

        BillingSummaryDto summary = medicalRecordService.getBillingSummary(medicalRecordId);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=bill_" + medicalRecordId + ".pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        BaseFont baseFont = BaseFont.createFont("fonts/DejaVuSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font titleFont = new Font(baseFont, 16, Font.BOLD);
        Font normalFont = new Font(baseFont, 12);

        document.add(new Paragraph("🧾 HÓA ĐƠN THANH TOÁN VIỆN PHÍ", titleFont));
        document.add(new Paragraph("Ngày in: " + LocalDate.now(), normalFont));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("👤 Bệnh nhân: " + summary.getPatientName(), normalFont));
        document.add(new Paragraph("📄 Mã hồ sơ: " + summary.getMedicalRecordCode(), normalFont));
        document.add(new Paragraph("💉 Tiền khám: " + summary.getMedicalFee() + " VND", normalFont));
        document.add(new Paragraph("🧪 Xét nghiệm: " + summary.getTestFee() + " VND", normalFont));
        document.add(new Paragraph("💊 Thuốc: " + summary.getMedicineFee() + " VND", normalFont));
        document.add(new Paragraph("💰 Tổng cộng: " + summary.getTotalFee() + " VND", normalFont));
        document.add(new Paragraph("💵 Tạm ứng: " + summary.getAdvancePayment() + " VND", normalFont));
        document.add(new Paragraph("📉 Còn lại: " + summary.getRemainingAmount() + " VND", normalFont));

        document.close();
    }
}
