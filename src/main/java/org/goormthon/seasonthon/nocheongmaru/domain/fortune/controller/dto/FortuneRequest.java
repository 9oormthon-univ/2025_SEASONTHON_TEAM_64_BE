package org.goormthon.seasonthon.nocheongmaru.domain.fortune.controller.dto;

import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;

public record FortuneRequest(
	String description,
	Member sender
) {}