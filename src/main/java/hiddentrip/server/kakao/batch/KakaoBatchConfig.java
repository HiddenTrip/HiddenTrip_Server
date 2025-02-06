package hiddentrip.server.kakao.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import hiddentrip.server.batch.runner.BatchExecutionTimeListener;
import hiddentrip.server.kakao.map.tasklet.clear.ClearKakaoMapTasklet;
import hiddentrip.server.kakao.map.tasklet.fetch.FetchKakaoMapTasklet;
import hiddentrip.server.kakao.map.tasklet.process.ProcessKakaoMapTasklet;
import hiddentrip.server.kakao.map.tasklet.save.SaveKakaoMapTasklet;
import hiddentrip.server.kakao.review.tasklet.clear.ClearKakaoReviewTasklet;
import hiddentrip.server.kakao.review.tasklet.fetch.FetchKakaoReviewTasklet;
import hiddentrip.server.kakao.review.tasklet.process.ProcessKakaoReviewTasklet;
import hiddentrip.server.kakao.review.tasklet.save.SaveKakaoReviewTasklet;
import hiddentrip.server.kakao.review.tasklet.save.SaveReviewKeyWordTasklet;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class KakaoBatchConfig {
	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final BatchExecutionTimeListener batchExecutionTimeListener;

	private final ClearKakaoReviewTasklet clearKakaoReviewTasklet;
	private final ClearKakaoMapTasklet clearKakaoMapTasklet;

	private final FetchKakaoMapTasklet fetchKakaoMapTasklet;
	private final ProcessKakaoMapTasklet processKakaoMapTasklet;
	private final SaveKakaoMapTasklet saveKakaoMapTasklet;

	private final FetchKakaoReviewTasklet fetchKakaoReviewTasklet;
	private final ProcessKakaoReviewTasklet processKakaoReviewTasklet;
	private final SaveKakaoReviewTasklet saveKakaoReviewTasklet;

	private final SaveReviewKeyWordTasklet saveReviewKeyWordTasklet;

	@Bean
	public Job kakaoDataJob() {
		return new JobBuilder("kakaoDataJob", jobRepository)
			.listener(batchExecutionTimeListener) // 실행 시간 측정 리스너 등록
			.start(clearKakaoReviewStep()) // Step 0: 카카오 DB 전체 초기화
			.next(clearKakaoMapStep())

			.next(fetchKakaoAPIStep())  // Step 1: 카카오 API에서 데이터 가져오기
			.next(processKakaoAPIStep()) // Step 2: 카카오 API 데이터 가공하기
			.next(saveKakaoAPIStep())    // Step 3: 카카오 API 데이터 저장하기

			.next(fetchKakaoReviewStep())  // Step 4: 크롤링 데이터 가져오기
			.next(processKakaoReviewStep())  // Step 5: 크롤링 데이터 가공하기
			.next(saveKakaoReviewStep())  // Step 6: 크롤링 데이터 저장하기

			.next(saveKeyWordStep()) // Step 7: 키워드 추출 후 데이터 저장하기

			.build();
	}

	@Bean
	public Step clearKakaoReviewStep() {
		return new StepBuilder("clearKakaoReviewStep", jobRepository)
			.tasklet(clearKakaoReviewTasklet, transactionManager)
			.allowStartIfComplete(true) // 실패한 경우 다시 실행 가능
			.build();
	}

	@Bean
	public Step clearKakaoMapStep() {
		return new StepBuilder("clearKakaoMapStep", jobRepository)
			.tasklet(clearKakaoMapTasklet, transactionManager)
			.allowStartIfComplete(true) // 실패한 경우 다시 실행 가능
			.build();
	}

	@Bean
	public Step fetchKakaoAPIStep() {
		return new StepBuilder("fetchKakaoAPIStep", jobRepository)
			.tasklet(fetchKakaoMapTasklet, transactionManager)
			.allowStartIfComplete(true) // 실패한 경우 다시 실행 가능
			.build();
	}

	@Bean
	public Step processKakaoAPIStep() {
		return new StepBuilder("processKakaoAPIStep", jobRepository)
			.tasklet(processKakaoMapTasklet, transactionManager)
			.allowStartIfComplete(true)
			.build();
	}

	@Bean
	public Step saveKakaoAPIStep() {
		return new StepBuilder("saveKakaoAPIStep", jobRepository)
			.tasklet(saveKakaoMapTasklet, transactionManager)
			.allowStartIfComplete(true)
			.build();
	}

	@Bean
	public Step fetchKakaoReviewStep() {
		return new StepBuilder("fetchKakaoReviewStep", jobRepository)
			.tasklet(fetchKakaoReviewTasklet, transactionManager)
			.allowStartIfComplete(true)
			.build();
	}

	@Bean
	public Step processKakaoReviewStep() {
		return new StepBuilder("processKakaoReviewStep", jobRepository)
			.tasklet(processKakaoReviewTasklet, transactionManager)
			.allowStartIfComplete(true)
			.build();
	}

	@Bean
	public Step saveKakaoReviewStep() {
		return new StepBuilder("saveKakaoReviewStep", jobRepository)
			.tasklet(saveKakaoReviewTasklet, transactionManager)
			.allowStartIfComplete(true)
			.build();
	}

	@Bean
	public Step saveKeyWordStep() {
		return new StepBuilder("saveReviewKeyWordStep", jobRepository)
			.tasklet(saveReviewKeyWordTasklet, transactionManager)
			.allowStartIfComplete(true)
			.build();
	}
}
