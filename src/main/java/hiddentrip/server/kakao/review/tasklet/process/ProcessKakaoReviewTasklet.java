package hiddentrip.server.kakao.review.tasklet.process;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import hiddentrip.server.kakao.review.dto.KakaoReviewDataHolder;
import hiddentrip.server.kakao.review.entity.KakaoReviewEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessKakaoReviewTasklet implements Tasklet {

	private final KakaoReviewDataHolder kakaoReviewDataHolder;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		if (kakaoReviewDataHolder.getRawData().isEmpty()) {
			return RepeatStatus.FINISHED;
		}

		for (String jsonResponse : kakaoReviewDataHolder.getRawData()) {
			JsonNode rootNode = objectMapper.readTree(jsonResponse);
			JsonNode comments = rootNode.path("comment").path("list");

			long storeId = rootNode.path("storeId").asLong();

			comments.forEach(comment -> {
				String contents = comment.path("contents").asText();

				if (contents.isEmpty()) {
					log.warn("⚠️ 리뷰 없음, storeId: {}", storeId);
					return;
				}

				KakaoReviewEntity kakaoReviewEntity = KakaoReviewEntity.builder()
					.storeId(storeId)
					.userName(comment.path("username").asText())
					.point(comment.path("point").asDouble())
					.contents(contents)
					.date(comment.path("date").asText())
					.build();

				kakaoReviewDataHolder.addProcessedData(kakaoReviewEntity);
			});
		}

		return RepeatStatus.FINISHED;
	}

}
