package hiddentrip.server.kakao.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hiddentrip.server.kakao.map.entity.KakaoMapEntity;

@Repository
public interface KakaoMapJpaRepository extends JpaRepository<KakaoMapEntity, Long> {

}
