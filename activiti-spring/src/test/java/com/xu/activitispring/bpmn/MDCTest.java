package com.xu.activitispring.bpmn;

import org.activiti.engine.TaskService;
import org.activiti.engine.logging.LogMDC;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xuhongda on 2018/12/6
 * com.xu.activitispring.bpmn
 * activiti-xu
 */

public class MDCTest {
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = "VacationRequest.bpmn20.xml")
    public void contextLoads() {
        LogMDC.setMDCEnabled(true);
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        activitiRule.getTaskService().complete(task.getId());
    }
}
