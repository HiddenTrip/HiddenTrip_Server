package hiddentrip.server.kakao.review.tasklet.save;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import hiddentrip.server.kakao.review.entity.KakaoReviewEntity;
import hiddentrip.server.kakao.review.repository.KakaoReviewRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SaveReviewKeyWordTasklet implements Tasklet {
	private final KakaoReviewRepository kakaoReviewRepository;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		List<KakaoReviewEntity> kakaoReviewEntities = kakaoReviewRepository.findAll();

		List<KakaoReviewEntity> updatedEntities = kakaoReviewEntities.stream()
			.peek(kakaoReviewEntity -> {
				Long friend = kakaoReviewRepository.getKeyWordCount(kakaoReviewEntity, "친구");
				Long family = kakaoReviewRepository.getKeyWordCount(kakaoReviewEntity, "가족");
				Long lover = kakaoReviewRepository.getKeyWordCount(kakaoReviewEntity, "연인");

				kakaoReviewEntity.update(friend, family, lover);
			})
			.collect(Collectors.toList());

		// ✅ Bulk Insert (일괄 저장)
		kakaoReviewRepository.bulkInsert(updatedEntities);

		return RepeatStatus.FINISHED;
	}
}