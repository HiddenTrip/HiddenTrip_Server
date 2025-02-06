package hiddentrip.server.kakao.review.entity;

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
@Table(name = "KAKAO_REVIEW")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Builder
public class KakaoReviewEntity {
	@Id
	@Column(name = "kakao_review_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long kakaoReviewSeq;

	@Column(name = "store_id", nullable = false)
	private Long storeId;

	@Column(name = "user_name", nullable = false)
	private String userName;

	@Column(name = "point", nullable = false)
	private double point;

	@Column(columnDefinition = "TEXT", name = "contents", nullable = false)
	private String contents;

	@Column(name = "date", nullable = false)
	private String date;

	@Column(name = "friend", columnDefinition = "BIGINT DEFAULT 0")
	private Long friend;

	@Column(name = "family", columnDefinition = "BIGINT DEFAULT 0")
	private Long family;

	@Column(name = "lover", columnDefinition = "BIGINT DEFAULT 0")
	private Long lover;

	public KakaoReviewEntity update(Long friend, Long family, Long lover) {
		this.friend = friend;
		this.family = family;
		this.lover = lover;

		return this;
	}
}
