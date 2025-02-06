package hiddentrip.server.kakao.review.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import hiddentrip.server.kakao.review.entity.KakaoReviewEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class KakaoReviewDataHolder {
	private List<String> rawData = new ArrayList<>();
	private List<KakaoReviewEntity> processedData = new ArrayList<>();

	// 원본 JSON 데이터 저장
	public void addRawData(String data) {
		rawData.add(data);
	}

	// 가공된 리뷰 데이터 저장
	public void addProcessedData(KakaoReviewEntity review) {
		processedData.add(review);
	}

	// 데이터 초기화 (Batch 실행 후 메모리 정리)
	public void clearData() {
		rawData.clear();
		processedData.clear();
	}
}
