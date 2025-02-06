package hiddentrip.server.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync(proxyTargetClass = true) // CGLIB Proxy 사용
public class AsyncConfig {

	@Bean(name = "asyncExecutor")
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20);  // 최소 20개의 스레드
		executor.setMaxPoolSize(50);   // 최대 50개의 스레드
		executor.setQueueCapacity(200); // 대기열 크기 200으로 설정
		executor.setThreadNamePrefix("AsyncThread-");
		executor.initialize();
		return executor;
	}
}