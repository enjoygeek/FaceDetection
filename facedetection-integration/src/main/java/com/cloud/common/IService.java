package com.cloud.common;

import com.cloud.dto.Image;
import com.cloud.dto.ProcessResult;

public interface IService {

	public ProcessResult run(Image image);

}
