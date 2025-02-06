package hiddentrip.server.kakao.review.tasklet.clear;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import hiddentrip.server.kakao.review.repository.KakaoReviewRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClearKakaoReviewTasklet implements Tasklet {

	private final KakaoReviewRepository kakaoReviewRepository;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		kakaoReviewRepository.deleteAll();

		return RepeatStatus.FINISHED;
	}
}