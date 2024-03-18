package com.sakute.project_fumo_backend.repository.repository;

import com.sakute.project_fumo_backend.repository.repository.impl.*;

public interface RepositoryFactory {
    UserRepository getUserRepository();
    CommentRepository getCommentRepository();
    DonationsRepository getDonationsRepository();
    FundraisingCategoryRepository getFundraisingCategoryRepository();
    FundraisingRepository getFundraisingRepository();
    FundraisingCommentsRepository getFundraisingCommentsRepository();
    FundraisingGoalsRepository getFundraisingGoalsRepository();
    FundraisingLikesRepository getFundraisingLikesRepository();
    FundraisingUpdatesRepository getFundraisingUpdatesRepository();
    IntellectualPropertyCommentsRepository getIntellectualPropertyCommentsRepository();
    IntellectualPropertyLikesRepository getIntellectualPropertyLikesRepository();
    OrdersRepository getOrdersRepository();
    PostTagTopicRepository getPostTagTopicRepository();
    ReportingRepository getReportingRepository();
    RoleRepository getRoleRepository();
    UserPostLikeRepository getUserPostLikeRepository();
    UserPostRepository getUserPostRepository();
    UserProfilesRepository getUserProfilesRepository();
    UserSettingsRepository getUserSettingsRepository();
    IntellectualPropertyCategoryRepository getIntellectualPropertyCategoryRepository();
    IntellectualPropertyRepository getIntellectualPropertyRepository();
    LicensingAgreementsRepository getLicensingAgreementsRepository();

}
