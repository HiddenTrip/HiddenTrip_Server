package hiddentrip.server.kakao.map.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import hiddentrip.server.kakao.map.entity.KakaoMapEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class KakaoMapDataHolder {
	private List<String> rawData = new ArrayList<>();
	private List<KakaoMapEntity> processedData = new ArrayList<>();

	// API에서 받은 원본 데이터 저장
	public void addRawData(String data) {
		rawData.add(data);
	}

	// 가공된 데이터 저장
	public void addProcessedData(KakaoMapEntity restaurant) {
		processedData.add(restaurant);
	}

	// 데이터 초기화 (Batch 실행 후 메모리 정리용)
	public void clearData() {
		rawData.clear();
		processedData.clear();
	}
}
