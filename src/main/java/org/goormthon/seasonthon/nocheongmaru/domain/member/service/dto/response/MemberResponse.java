package org.goormthon.seasonthon.nocheongmaru.domain.member.service.dto.response;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;

public record MemberResponse(
	Long id,
	String nickname,
	String profileImageUrl
) {
	public static MemberResponse of(Member member) {
		return new MemberResponse(
			member.getId(),
			member.getNickname(),
			member.getProfileImageURL()
		);
	}
}
