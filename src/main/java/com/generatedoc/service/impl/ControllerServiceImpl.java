package com.generatedoc.service.impl;

import com.generatedoc.constant.SpringMVCConstant;
import com.generatedoc.service.ControllerService;
import org.springframework.stereotype.Service;

@Service
public class ControllerServiceImpl implements ControllerService {
    @Override
    public boolean isControlAnnotation(String annotatianName) {
        return SpringMVCConstant.CONTROLLER_NAMES.contains(annotatianName);
    }
}
