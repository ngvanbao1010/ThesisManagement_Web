/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.nhom23.services;

import com.nhom23.repositories.ThesisRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.thymeleaf.context.Context;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 *
 * @author Administrator
 */
@Service
public class PDFService {

    @Autowired
    private ThesisRepository thesisRepository;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public byte[] generatePdfFromHtml(String htmlContent) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(outputStream);

            String fontPath = getClass().getClassLoader().getResource("fonts/DejaVuSans.ttf").getFile();
            builder.useFont(new File(fontPath), "dejavusans");

            builder.run();
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] exportThesisReport(int thesisId) {
        List<Object[]> rawData = thesisRepository.getThesisScoreSheet(thesisId);

        if (rawData == null || rawData.isEmpty()) {
            return null;
        }

        Map<String, Object> data = new HashMap<>();

        Object[] firstRow = rawData.get(0);
        data.put("title", firstRow[1]);
        data.put("defenseDate", firstRow[2]);
        data.put("file", firstRow[3]);

        Map<String, Map<String, String>> studentMap = new LinkedHashMap<>();
        for (Object[] row : rawData) {
            String code = (String) row[5];      
            if (!studentMap.containsKey(code)) {
                Map<String, String> s = new HashMap<>();
                s.put("name", (String) row[4]);
                s.put("studentCode", code);
                s.put("email", (String) row[6]);
                s.put("phone", (String) row[7]);
                s.put("major", (String) row[8]);
                studentMap.put(code, s);
            }
        }
        data.put("students", new ArrayList<>(studentMap.values()));

        List<Map<String, Object>> details = new ArrayList<>();
        double totalScore = 0;
        int count = 0;

        for (Object[] row : rawData) {
            if (row.length >= 11) {
                Map<String, Object> item = new HashMap<>();
                item.put("lecturer", row[9]);
                item.put("criterion", row[10]);
                item.put("score", row[11]);
                details.add(item);

                totalScore += Double.parseDouble(row[11].toString());
                count++;
            } else {
                System.out.println("⚠️ Dòng dữ liệu không hợp lệ, chỉ có " + row.length + " phần tử: " + Arrays.toString(row));
            }
        }
        data.put("details", details);

        double avgScore = count > 0 ? totalScore / count : 0;
        data.put("avgScore", String.format("%.2f", avgScore));
        data.put("grade", getGrade(avgScore));

        Context context = new Context();
        context.setVariables(data);
        String htmlContent = templateEngine.process("report_template", context);

        return generatePdfFromHtml(htmlContent);
    }

    private String getGrade(double avg) {
        if (avg >= 9) {
            return "Xuất sắc";
        }
        if (avg >= 8) {
            return "Giỏi";
        }
        if (avg >= 7) {
            return "Khá";
        }
        if (avg >= 5) {
            return "Trung bình";
        }
        return "Yếu";
    }
}
