package com.changxiang.game.sdk.exception;

public class CXCallbackListenerNullException extends Exception {


	private static final long serialVersionUID = -8831463422132317310L;      

	public CXCallbackListenerNullException(String detailMessage, Throwable throwable)
	  {
	    super(detailMessage, throwable);
	  }

	  public CXCallbackListenerNullException(String detailMessage)
	  {
	    super(detailMessage);
	  }

	  public CXCallbackListenerNullException(Throwable throwable)
	  {
	    super(throwable);
	  }

	  public CXCallbackListenerNullException()
	  {
	  }
}
