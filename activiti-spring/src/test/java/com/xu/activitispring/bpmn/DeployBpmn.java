package com.xu.activitispring.bpmn;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xuhongda on 2018/11/29
 * com.xu.activitispring.bpmn
 * activiti-xu
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class DeployBpmn {


    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ManagementService managementService;

    /**
     * 部署一个流程
     */
    @Test
    public void dy() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .addClasspathResource("bpmn/xxx.bpmn20.xml")
                .deploy();

        log.info("Number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());
    }

    /**
     * 启动一个流程
     */
    @Test
    public void start() {
        Map<String, Object> variables = new HashMap<>(3);
        variables.put("employeeName", "Kermit");
        variables.put("numberOfDays", new Integer(4));
        variables.put("vacationMotivation", "I'm really tired!");
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vacationRequest", variables);
        log.info("流程{}启动", processInstance.getName());
        // Verify that we started a new process instance
        log.info("Number of process instances: " + runtimeService.createProcessInstanceQuery().count());

    }
}
