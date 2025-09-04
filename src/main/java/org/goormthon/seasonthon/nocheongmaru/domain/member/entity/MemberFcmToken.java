package org.goormthon.seasonthon.nocheongmaru.domain.member.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_fcm_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberFcmToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(nullable = false, unique = true)
	private String token;

	private LocalDateTime createdAt = LocalDateTime.now();

	@Builder
	public MemberFcmToken(Member member, String token) {
		this.member = member;
		this.token = token;
	}
}
