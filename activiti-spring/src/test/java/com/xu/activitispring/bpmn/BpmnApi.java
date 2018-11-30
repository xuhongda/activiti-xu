package com.xu.activitispring.bpmn;

import org.activiti.engine.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xuhongda on 2018/11/29
 * com.xu.activitispring.bpmn
 * activiti-xu
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class BpmnApi {
    @Test
    public void test1() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        TaskService taskService = processEngine.getTaskService();
        ManagementService managementService = processEngine.getManagementService();
        IdentityService identityService = processEngine.getIdentityService();
        HistoryService historyService = processEngine.getHistoryService();
        FormService formService = processEngine.getFormService();
        DynamicBpmnService dynamicBpmnService = processEngine.getDynamicBpmnService();

    }
}
