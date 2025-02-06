package hiddentrip.server.kakao.map.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import hiddentrip.server.kakao.map.entity.KakaoMapEntity;

@Component
public interface KakaoMapRepository {

	void bulkInsert(List<KakaoMapEntity> kakaoMapEntities);

	List<KakaoMapEntity> findAll();

	void deleteAll();
}
