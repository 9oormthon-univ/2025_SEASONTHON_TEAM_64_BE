package org.goormthon.seasonthon.nocheongmaru.global.exception.feed;

import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

import lombok.Getter;

@Getter
public class FeedNotFoundException extends BaseException {

  public FeedNotFoundException() {
    super(ErrorCode.FEED_NOT_FOUND);
  }

  public FeedNotFoundException(Throwable cause) {
    super(ErrorCode.FEED_NOT_FOUND, cause);
  }
}
