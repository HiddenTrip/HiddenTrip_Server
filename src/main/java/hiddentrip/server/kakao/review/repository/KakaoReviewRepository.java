package hiddentrip.server.kakao.review.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import hiddentrip.server.kakao.review.entity.KakaoReviewEntity;

@Component
public interface KakaoReviewRepository {

	void bulkInsert(List<KakaoReviewEntity> kakaoReviewEntities);

	List<KakaoReviewEntity> findAll();

	Long getKeyWordCount(KakaoReviewEntity kakaoReviewEntity, String keyWord);

	void deleteAll();
}
