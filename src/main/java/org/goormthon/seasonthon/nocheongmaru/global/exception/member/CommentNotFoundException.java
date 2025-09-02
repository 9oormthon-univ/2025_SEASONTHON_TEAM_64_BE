package org.goormthon.seasonthon.nocheongmaru.global.exception.member;

import org.goormthon.seasonthon.nocheongmaru.global.exception.common.BaseException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.common.ErrorCode;

public class CommentNotFoundException extends BaseException {

  public CommentNotFoundException() {
    super(ErrorCode.COMMENT_NOT_FOURN);
  }

  public CommentNotFoundException(Throwable cause) {
    super(ErrorCode.COMMENT_NOT_FOURN, cause);
  }
}
