package hiddentrip.server.kakao.review.tasklet.fetch;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import hiddentrip.server.kakao.map.entity.KakaoMapEntity;
import hiddentrip.server.kakao.map.repository.KakaoMapRepository;
import hiddentrip.server.kakao.review.dto.KakaoReviewDataHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class FetchKakaoReviewTasklet implements Tasklet {

	private static final String API_URL = "https://place.map.kakao.com/commentlist/v/";
	private final KakaoMapRepository kakaoMapRepository;
	private final KakaoReviewDataHolder kakaoReviewDataHolder;
	private final Executor asyncExecutor;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		List<KakaoMapEntity> kakaoMapEntities = kakaoMapRepository.findAll();

		List<List<KakaoMapEntity>> batches = Lists.partition(kakaoMapEntities, 10);
		batches.forEach(batch -> {
			CompletableFuture.allOf(batch.stream()
				.map(this::fetchReviewsByStoreId).toArray(CompletableFuture[]::new)).join();
		});

		return RepeatStatus.FINISHED;
	}

	@Async("asyncExecutor")
	public CompletableFuture<Void> fetchReviewsByStoreId(KakaoMapEntity kakaoMapEntity) {
		return CompletableFuture.runAsync(() -> {
			Long storeId = Long.valueOf(kakaoMapEntity.getId());

			try {
				Connection connect = Jsoup.connect(API_URL + storeId)
					.header("Content-Type", "application/json")
					.ignoreContentType(true)
					.timeout(5000);

				String json = connect.execute().body();

				// ✅ storeId를 JSON 데이터와 함께 저장
				JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
				jsonObject.addProperty("storeId", storeId); // storeId를 추가
				kakaoReviewDataHolder.addRawData(jsonObject.toString());

				return;

			} catch (Exception e) {
				log.error("❌ 크롤링 실패 storeId: {}: {}", storeId, e.getMessage());
			}
		}, asyncExecutor);
	}
}
