package hiddentrip.server.kakao.map.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "KAKAO_MAP")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Builder
public class KakaoMapEntity {

	@Id
	@Column(name = "kakao_map_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long kakaoMapSeq;

	@Column(name = "address_name")
	private String addressName;

	@Column(name = "category_group_code")
	private String categoryGroupCode;

	@Column(name = "category_group_name")
	private String categoryGroupName;

	@Column(name = "category_name")
	private String categoryName;

	@Column(name = "distance")
	private String distance;

	@Column(name = "id")
	private String id;

	@Column(name = "phone")
	private String phone;

	@Column(name = "place_name")
	private String placeName;

	@Column(name = "place_url")
	private String placeUrl;

	@Column(name = "road_address_name")
	private String roadAddressName;

	@Column(name = "x")
	private String x;

	@Column(name = "y")
	private String y;
}
