package com.jeeframework.logicframework.common.logging;

import com.jeeframework.core.exception.BaseException;

/**

 * 
 */

public class LoggingException extends BaseException {

  public LoggingException(  String msg ) {
    super(msg );
  }

  public LoggingException(String msg, Throwable e ) {
    super( msg, e);
  }


}