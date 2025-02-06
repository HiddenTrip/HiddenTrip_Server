package hiddentrip.server.kakao.review.tasklet.save;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import hiddentrip.server.kakao.review.dto.KakaoReviewDataHolder;
import hiddentrip.server.kakao.review.repository.KakaoReviewRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SaveKakaoReviewTasklet implements Tasklet {

	private final KakaoReviewRepository kakaoReviewRepository;
	private final KakaoReviewDataHolder kakaoReviewDataHolder;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		kakaoReviewRepository.bulkInsert(kakaoReviewDataHolder.getProcessedData());
		kakaoReviewDataHolder.clearData();

		return RepeatStatus.FINISHED;
	}
}
