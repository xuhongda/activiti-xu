package com.xu.activitispring.bpmn;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
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
@ContextConfiguration(locations = {"classpath:config/activiti.context.xml"})
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
    @Autowired
    private RuntimeService runtimeService;


    /**
     * 部署一个流程
     * 需要注意mysql 连接驱动版本
     * <p>
     *     在 act_ge_bytearray 表中会留下部署文件 x.bpmn20.xml 和 x.png 信息
     *     ...
     *     在 act_re_xxx  表
     *
     * </p>
     */
    @Test
    public void dy() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();

        Deployment deploy = deploymentBuilder.name("jg")
                .addClasspathResource("bpmn/myOne.bpmn20.xml")
                .deploy();
        log.info("流程部署对象:{}", deploy);
        log.info("Number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());
    }


    /**
     * 暂停一个任务,任务被暂停又被开启，会抛出异常
     */
    @Test
    public void suspended() {
        repositoryService.suspendProcessDefinitionByKey("vacationRequest");
        try {
            runtimeService.startProcessInstanceByKey("vacationRequest");
        } catch (ActivitiException e) {
            e.printStackTrace();
        }
        //需要用 activateProcessDefinitionXXX 激活
        repositoryService.activateProcessDefinitionByKey("vacationRequest");
    }


    /**
     * 启动一个流程
     */
    @Test
    public void start() {
        Map<String, Object> variables = new HashMap<>(3);
        variables.put("employeeName", "Kermit");
        variables.put("numberOfDays", 4);
        variables.put("vacationMotivation", "I'm really tired!");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my_activiti_one", variables);
        log.info("流程{}启动", processInstance.getProcessDefinitionName());
        // Verify that we started a new process instance
        log.info("Number of process instances: " + runtimeService.createProcessInstanceQuery().count());
    }

    /**
     * 查看任务
     */
    @Test
    public void task() {
        // Fetch all tasks for the management group
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        // List<Task> list = taskService.createTaskQuery().list();
        for (Task task : tasks) {
            log.info("Task available: " + task.getName());
        }
        log.info("任务数量{}", tasks.size());
    }

    /**
     * 执行审批
     */
    @Test
    public void complete() {

        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        for (Task task : tasks) {
            log.info("Task available: " + task.getName());
        }

        Task task = tasks.get(0);

        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("vacationApproved", "false");
        taskVariables.put("managerMotivation", "We have a tight deadline!");
        taskService.complete(task.getId(), taskVariables);

    }


}
