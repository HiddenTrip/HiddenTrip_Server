package hiddentrip.server.kakao.map.repository;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import hiddentrip.server.kakao.map.entity.KakaoMapEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoMapRepositoryImpl implements KakaoMapRepository {
	private final KakaoMapJpaRepository kakaoMapJpaRepository;

	@PersistenceContext
	private final EntityManager entityManager;

	@Override
	@Transactional
	public void bulkInsert(List<KakaoMapEntity> kakaoMapEntities) {
		int batchSize = 1000;

		for (int i = 0; i < kakaoMapEntities.size(); i++) {
			KakaoMapEntity entity = kakaoMapEntities.get(i);

			if (entity.getKakaoMapSeq() == null) { // ID가 없으면 신규 데이터 → persist
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
	public List<KakaoMapEntity> findAll() {
		return kakaoMapJpaRepository.findAll();
	}

	@Override
	public void deleteAll() {
		kakaoMapJpaRepository.deleteAll();
	}
}
