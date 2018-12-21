package com.generatedoc.service;

import com.generatedoc.entity.ApiInterface;

public interface MarkDownMethodService {
    void buildMethodDoc(ApiInterface apiInterface, StringBuilder sb, int index);
}
