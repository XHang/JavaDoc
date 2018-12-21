package com.generatedoc.example;

import com.gddxit.dxbase.core.mvc.result.BaseResult;
import com.gddxit.dxbase.workflow.form.WorkflowForm;
import com.gddxit.wwis.flowapp.service.KmSiteChangeService;
import com.gddxit.wwis.flowapp.service.KmUsewaterUsenatureChangeService;
import com.gddxit.wwis.flowapp.service.UsewaterFeetypeChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 示例接口
 * @author cxh
 */
@RestController
@RequestMapping("/example")
public class ExampleControl {

    @Autowired
    private Service service;

    @RequestMapping(path = "/save",method = {RequestMethod.POST})
    public Result save(@RequestBody Form form){
        service.save(workflowForm);
        return new Result();
    }
}
