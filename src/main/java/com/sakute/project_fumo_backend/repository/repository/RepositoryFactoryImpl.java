package com.sakute.project_fumo_backend.repository.repository;

import com.sakute.project_fumo_backend.repository.repository.impl.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class RepositoryFactoryImpl implements RepositoryFactory {

    @PersistenceContext
    private final EntityManager entityManager;

    public RepositoryFactoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * @return
     */
    @Override
    public UserRepository getUserRepository() {
        return null;

    }

    /**
     * @return
     */
    @Override
    public CommentRepository getCommentRepository() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public DonationsRepository getDonationsRepository() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public FundraisingCategoryRepository getFundraisingCategoryRepository() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public FundraisingRepository getFundraisingRepository() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public FundraisingCommentsRepository getFundraisingCommentsRepository() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public FundraisingGoalsRepository getFundraisingGoalsRepository() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public FundraisingLikesRepository getFundraisingLikesRepository() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public FundraisingUpdatesRepository getFundraisingUpdatesRepository() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public IntellectualPropertyCommentsRepository getIntellectualPropertyCommentsRepository() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public IntellectualPropertyLikesRepository getIntellectualPropertyLikesRepository() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public OrdersRepository getOrdersRepository() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public PostTagTopicRepository getPostTagTopicRepository() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public ReportingRepository getReportingRepository() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public RoleRepository getRoleRepository() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public UserPostLikeRepository getUserPostLikeRepository() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public UserPostRepository getUserPostRepository() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public UserProfilesRepository getUserProfilesRepository() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public UserSettingsRepository getUserSettingsRepository() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public IntellectualPropertyCategoryRepository getIntellectualPropertyCategoryRepository() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public IntellectualPropertyRepository getIntellectualPropertyRepository() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public LicensingAgreementsRepository getLicensingAgreementsRepository() {
        return null;
    }
}
