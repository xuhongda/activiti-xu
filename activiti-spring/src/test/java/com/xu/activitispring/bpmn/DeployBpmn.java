package com.xu.activitispring.bpmn;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
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
    private ProcessEngine processEngine;
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
                .addClasspathResource("bpmn/myOne.bpmn20.xml")
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

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my_activiti_one", variables);
        log.info("流程{}启动", processInstance.getProcessDefinitionName());
        // Verify that we started a new process instance
        log.info("Number of process instances: " + runtimeService.createProcessInstanceQuery().count());
    }

    @Test
    public void task() {
        // Fetch all tasks for the management group
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        for (Task task : tasks) {
            log.info("Task available: " + task.getName());
        }
    }
}
