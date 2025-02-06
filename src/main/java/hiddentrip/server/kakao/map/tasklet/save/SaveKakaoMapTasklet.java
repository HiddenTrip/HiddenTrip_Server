package hiddentrip.server.kakao.map.tasklet.save;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import hiddentrip.server.kakao.map.dto.KakaoMapDataHolder;
import hiddentrip.server.kakao.map.repository.KakaoMapRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SaveKakaoMapTasklet implements Tasklet {

	private final KakaoMapRepository kakaoMapRepository;
	private final KakaoMapDataHolder kakaoMapDataHolder;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		kakaoMapRepository.bulkInsert(kakaoMapDataHolder.getProcessedData());
		kakaoMapDataHolder.clearData();

		return RepeatStatus.FINISHED;
	}
}