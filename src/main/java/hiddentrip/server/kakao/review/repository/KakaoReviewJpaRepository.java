package hiddentrip.server.kakao.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hiddentrip.server.kakao.review.entity.KakaoReviewEntity;

@Repository
public interface KakaoReviewJpaRepository extends JpaRepository<KakaoReviewEntity, Long> {

}
