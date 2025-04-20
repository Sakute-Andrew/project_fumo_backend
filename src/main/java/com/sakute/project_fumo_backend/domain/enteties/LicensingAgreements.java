package com.sakute.project_fumo_backend.domain.enteties;

import com.sakute.project_fumo_backend.domain.enteties.intprop.IntellectualProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Table(name = "licensing_agreements")
public class LicensingAgreements {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "agreement_id")
    private UUID agreementId;

    @Column(name = "ip_id", nullable = false)
    private UUID ipId;

    @Column(name = "license_type", nullable = false)
    private String licenseType;

    @Column(name = "terms", nullable = false)
    private String terms;

    @Column(name = "start_date", nullable = false)
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;

    @ManyToOne
    @JoinColumn(name = "ip_id", insertable = false, updatable = false)
    private IntellectualProperty intellectualProperty;

    // Додаткові конструктори, гетери та сетери можна додати за потребою
}

