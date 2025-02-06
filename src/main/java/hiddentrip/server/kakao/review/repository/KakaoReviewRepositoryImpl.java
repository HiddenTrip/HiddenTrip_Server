package hiddentrip.server.kakao.review.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;

import hiddentrip.server.kakao.review.entity.KakaoReviewEntity;
import hiddentrip.server.kakao.review.entity.QKakaoReviewEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoReviewRepositoryImpl implements KakaoReviewRepository {

	private final KakaoReviewJpaRepository kakaoReviewJpaRepository;

	@PersistenceContext
	private final EntityManager entityManager;

	private final JPAQueryFactory queryFactory;

	@Override
	public void bulkInsert(List<KakaoReviewEntity> kakaoReviewEntities) {
		int batchSize = 1000;

		for (int i = 0; i < kakaoReviewEntities.size(); i++) {
			KakaoReviewEntity entity = kakaoReviewEntities.get(i);

			if (entity.getKakaoReviewSeq() == null) { // ID가 없으면 신규 데이터 → persist
				entityManager.persist(entity);
			} else { // ID가 있으면 기존 데이터 → merge (업데이트)
				entityManager.merge(entity);
			}

			if (i > 0 && i % batchSize == 0) {
				entityManager.flush();
				entityManager.clear();
			}
		}

		// 마지막 남은 데이터 처리
		entityManager.flush();
		entityManager.clear();
	}

	@Override
	public List<KakaoReviewEntity> findAll() {
		return kakaoReviewJpaRepository.findAll();
	}

	@Override
	public Long getKeyWordCount(KakaoReviewEntity kakaoReviewEntity, String keyWord) {
		QKakaoReviewEntity qKakaoReviewEntity = QKakaoReviewEntity.kakaoReviewEntity;

		return queryFactory
			.select(qKakaoReviewEntity.count())
			.from(qKakaoReviewEntity)
			.where(
				qKakaoReviewEntity.storeId.eq(kakaoReviewEntity.getStoreId())
					.and(qKakaoReviewEntity.contents.contains(keyWord))
			)
			.fetchOne();
	}

	@Override
	public void deleteAll() {
		kakaoReviewJpaRepository.deleteAll();
	}
}
