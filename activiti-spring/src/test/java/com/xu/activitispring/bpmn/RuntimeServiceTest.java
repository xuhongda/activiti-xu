package com.xu.activitispring.bpmn;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Before;
import org.junit.Rule;
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

    @Test
    public void start() {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "v1");
        map.put("key2", "v2");
        String id = repositoryService.createProcessDefinitionQuery().latestVersion().singleResult().getId();
        log.info("部署ID:{}", id);
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(id, map);
        log.info("启动流程：{}", processInstance);


    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"bpmn/VacationRequest.bpmn20.xml"})
    public void start2() {
        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder().businessKey("buyBook").variable("name", "xu");
        ProcessInstance processInstance = processInstanceBuilder.start();

        log.info("{}", processInstance);
    }
}
