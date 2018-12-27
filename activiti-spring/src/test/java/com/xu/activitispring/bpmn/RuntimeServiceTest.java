package com.xu.activitispring.bpmn;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuhongda on 2018/12/12
 * com.xu.activitispring.bpmn
 * activiti-xu
 * <p>
 * 使用 activiti 配置风格
 * </p>
 */
@Slf4j
public class RuntimeServiceTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    private RepositoryService repositoryService;

    private RuntimeService runtimeService;

    @Before
    public void bf() {
        repositoryService = activitiRule.getRepositoryService();
        runtimeService = activitiRule.getRuntimeService();
    }

    @Test
    public void getDe() {
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        List<Deployment> list = deploymentQuery.orderByDeploymenTime().asc().list();
        log.info("部署流程：{}", list);
    }


    /**
     * 启动流程实例时带上参数
     */
    @Test
    public void start() {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "v1");
        map.put("key2", "v2");
        String id = repositoryService.createProcessDefinitionQuery().latestVersion().singleResult().getId();
        log.info("部署ID:{}", id);
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(id, map);
        log.info("启动流程：{}", processInstance);

        // 流程启动后获取变量
        Map<String, Object> variables = runtimeService.getVariables(processInstance.getId());
        log.info("{}", variables);
        // 设置变量
        runtimeService.setVariable(processInstance.getId(), "key3", "value3");


    }

    /**
     * 根据ProcessInstanceBuilder启动流程实例
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = {"bpmn/VacationRequest.bpmn20.xml"})
    public void start2() {
        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder().businessKey("buyBook").variable("name", "xu");
        ProcessInstance processInstance = processInstanceBuilder.processDefinitionKey("vacationRequest").start();
        log.info("{}", processInstance);
    }

    /**
     * 流程的挂起和激活
     */
    @Test
    public void suspendAndActivateTest() {

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("vacationRequest")
                .variableValueEquals("key1", "v1").list().get(0);
        String processInstanceId = processInstance.getProcessInstanceId();

        System.out.println(processInstance.isSuspended());
        //挂起流程实例
        runtimeService.suspendProcessInstanceById(processInstanceId);
        //验证是否挂起
        Assert.assertTrue(runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().isSuspended());

        //激活流程实例
        runtimeService.activateProcessInstanceById(processInstanceId);
        //验证是否激活
        Assert.assertTrue(!runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().isSuspended());
    }


    /**
     * 流程实例的删除
     */
    @Test
    public void deleteProcessInstanceTest() {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("vacationRequest").variableValueEquals("key1", "v1").list().get(0);
        String processInstanceId = processInstance.getProcessInstanceId();
        runtimeService.deleteProcessInstance(processInstanceId, "删除测试");
    }



}
