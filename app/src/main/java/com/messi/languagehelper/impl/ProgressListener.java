package com.messi.languagehelper.impl;

public interface ProgressListener {

	void update(long bytesRead, long contentLength, boolean done);
	
}
