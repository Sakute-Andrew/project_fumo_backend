package com.sakute.project_fumo_backend.domain.service.specification;

import com.sakute.project_fumo_backend.domain.enteties.intprop.IntellectualProperty;
import com.sakute.project_fumo_backend.domain.enteties.intprop.IpStatus;
import org.springframework.data.jpa.domain.Specification;

public class IntellectualPropertySpec {

    public static Specification<IntellectualProperty> hasTypeIp(String typeIp) {
        return (root, query, cb) ->
            typeIp == null || typeIp.isBlank()
                ? cb.conjunction()
                : cb.equal(cb.lower(root.get("typeIp")), typeIp.toLowerCase());
    }

    public static Specification<IntellectualProperty> hasStatus(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isBlank()) return cb.conjunction();
            try {
                IpStatus ipStatus = IpStatus.valueOf(status.toUpperCase());
                return cb.equal(root.get("status"), ipStatus);
            } catch (IllegalArgumentException e) {
                return cb.conjunction(); // невідомий статус — ігноруємо
            }
        };
    }

    public static Specification<IntellectualProperty> nameContains(String name) {
        return (root, query, cb) ->
            name == null || name.isBlank()
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<IntellectualProperty> hasCategory(Long categoryId) {
        return (root, query, cb) ->
            categoryId == null
                ? cb.conjunction()
                : cb.equal(root.get("intellectualPropertyCategory").get("categoryId"), categoryId);
    }
}