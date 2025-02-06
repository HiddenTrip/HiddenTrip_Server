package hiddentrip.server.kakao.map.tasklet.fetch;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import hiddentrip.server.kakao.map.dto.KakaoMapDataHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class FetchKakaoMapTasklet implements Tasklet {
	private static final List<String> PROVINCES = List.of(
		"서울", "경기", "인천", "강원", "충남", "충북", "전남", "전북", "경남", "경북", "제주"
	);
	private final WebClient webClient;
	private final KakaoMapDataHolder kakaoMapDataHolder;
	private final Executor asyncExecutor; // 비동기 실행을 위한 Executor 주입
	@Value("${kakao.api.key}")
	private String API_KEY;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// 각 지역별로 비동기 실행
		List<CompletableFuture<Void>> futures = PROVINCES.stream()
			.map(this::fetchRestaurantsByProvince)
			.toList();

		// 모든 비동기 작업이 완료될 때까지 대기
		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

		return RepeatStatus.FINISHED;
	}

	@Async("asyncExecutor")
	public CompletableFuture<Void> fetchRestaurantsByProvince(String province) {
		return CompletableFuture.runAsync(() -> {
			AtomicInteger page = new AtomicInteger(1);
			AtomicInteger totalFetched = new AtomicInteger(0);
			int pageSize = 15;
			int maxResultsPerProvince = 1000;

			while (totalFetched.get() < maxResultsPerProvince) {
				try {
					String response = webClient.get()
						.uri(uriBuilder -> uriBuilder
							.path("/v2/local/search/keyword.json")
							.queryParam("query", province + " 맛집")
							.queryParam("page", page.get())
							.queryParam("size", pageSize)
							.queryParam("sort", "accuracy")
							.build())
						.header(HttpHeaders.AUTHORIZATION, "KakaoAK " + API_KEY)
						.retrieve()
						.bodyToMono(String.class)
						.block();

					kakaoMapDataHolder.addRawData(response);
					totalFetched.addAndGet(pageSize);
					page.incrementAndGet();

					if (response != null && response.contains("\"is_end\":true")) {
						break;
					}
				} catch (Exception e) {
					log.warn("Error fetching restaurants in " + province + ": " + e.getMessage());
					break;
				}
			}
		}, asyncExecutor);
	}
}