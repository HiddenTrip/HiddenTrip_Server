package hiddentrip.server.batch.runner;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchRunner {

	private final JobLauncher jobLauncher;
	private final Job kakaoDataJob;

	@Bean
	public ApplicationRunner runBatchJob() {
		return args -> {
			log.info("🚀 Spring Boot 실행 후 Batch Job 자동 실행 중...");
			try {
				JobExecution jobExecution = jobLauncher.run(kakaoDataJob, new JobParameters());
				log.info("✅ Batch Job 실행 완료: " + jobExecution.getStatus());
			} catch (Exception e) {
				log.info("❌ Batch Job 실행 중 오류 발생: " + e.getMessage());
			}
		};
	}
}