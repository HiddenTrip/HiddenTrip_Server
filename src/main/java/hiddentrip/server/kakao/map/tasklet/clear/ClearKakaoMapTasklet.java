package hiddentrip.server.kakao.map.tasklet.clear;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import hiddentrip.server.kakao.map.repository.KakaoMapRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClearKakaoMapTasklet implements Tasklet {

	private final KakaoMapRepository kakaoMapRepository;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		kakaoMapRepository.deleteAll();

		return RepeatStatus.FINISHED;
	}
}