package com.sakute.project_fumo_backend.domain.enteties;


import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "reporting")
public class Reporting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "report_name", nullable = false)
    private String reportName;

    @Column(name = "report_type", nullable = false)
    private String reportType;

    @Column(name = "start_period", nullable = false)
    private Timestamp startPeriod;

    @Column(name = "end_period", nullable = false)
    private Timestamp endPeriod;

    @Column(name = "report_data", columnDefinition = "jsonb")
    private String reportData; // Тут змінено на String, так як JPA не працює без власного маппінгу для JSONB.

    @Column(name = "generated_at", nullable = false)
    private Timestamp generatedAt;

}

