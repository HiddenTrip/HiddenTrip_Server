package hiddentrip.server.batch.runner;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BatchExecutionTimeListener implements JobExecutionListener {

	private long jobStartTime;

	@Override
	public void beforeJob(JobExecution jobExecution) {
		jobStartTime = System.currentTimeMillis(); // 배치 시작 시간 기록
		log.info("🚀 Batch Job 시작: " + jobExecution.getJobInstance().getJobName());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		long jobEndTime = System.currentTimeMillis(); // 배치 종료 시간 기록
		long totalDuration = (jobEndTime - jobStartTime) / 1000; // 초 단위 변환

		log.info("✅ Batch Job 종료: " + jobExecution.getJobInstance().getJobName());
		log.info("⏳ 총 실행 시간: " + totalDuration + "초");

		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("🎉 배치 작업이 성공적으로 완료되었습니다.");
		} else {
			log.info("❌ 배치 작업이 실패했습니다.");
		}

		for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
			log.info("🔹 Step: " + stepExecution.getStepName());

			LocalDateTime start = stepExecution.getStartTime();
			LocalDateTime end = stepExecution.getEndTime();

			long stepDuration = 0;
			Instant startInstant = start.atZone(ZoneId.systemDefault()).toInstant();
			Instant endInstant = end.atZone(ZoneId.systemDefault()).toInstant();

			stepDuration = Duration.between(startInstant, endInstant).toMillis();

			log.info("   ⏱ 실행 시간: " + stepDuration + "ms");
			log.info("   🟢 완료된 아이템 수: " + stepExecution.getWriteCount());
			log.info("   🔴 실패한 아이템 수: " + stepExecution.getFailureExceptions().size());
		}
	}
}
