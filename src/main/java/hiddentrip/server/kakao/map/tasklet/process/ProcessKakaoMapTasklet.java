package hiddentrip.server.kakao.map.tasklet.process;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import hiddentrip.server.kakao.map.dto.KakaoMapDataHolder;
import hiddentrip.server.kakao.map.entity.KakaoMapEntity;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProcessKakaoMapTasklet implements Tasklet {

	private final KakaoMapDataHolder kakaoMapDataHolder;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		for (String jsonResponse : kakaoMapDataHolder.getRawData()) {
			JsonNode rootNode = objectMapper.readTree(jsonResponse);
			JsonNode documents = rootNode.path("documents");

			documents.forEach(document -> {
				KakaoMapEntity restaurant = KakaoMapEntity.builder()
					.addressName(document.path("address_name").asText())
					.categoryGroupCode(document.path("category_group_code").asText())
					.categoryGroupName(document.path("category_group_name").asText())
					.categoryName(document.path("category_name").asText())
					.distance(document.path("distance").asText())
					.id(document.path("id").asText())
					.phone(document.path("phone").asText())
					.placeName(document.path("place_name").asText())
					.placeUrl(document.path("place_url").asText())
					.roadAddressName(document.path("road_address_name").asText())
					.x(document.path("x").asText())
					.y(document.path("y").asText())
					.build();

				kakaoMapDataHolder.addProcessedData(restaurant);
			});
		}
		return RepeatStatus.FINISHED;
	}
}