package org.goormthon.seasonthon.nocheongmaru.domain.member.model.response;

import org.goormthon.seasonthon.nocheongmaru.domain.member.model.entity.Member;

public record MemberResponse(
	Long id,
	String email,
	String nickname,
	String profileImageUrl
) {
	public static MemberResponse of(Member member) {
		return new MemberResponse(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			member.getProfileImageURL()
		);
	}
}
