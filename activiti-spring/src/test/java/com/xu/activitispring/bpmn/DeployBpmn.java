package com.xu.activitispring.bpmn;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xuhongda on 2018/11/29
 * com.xu.activitispring.bpmn
 * activiti-xu
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class DeployBpmn {
    @Test
    public void dy() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .addClasspathResource("bpmn/xxx.bpmn20.xml")
                .deploy();

        log.info("Number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());
    }
}
