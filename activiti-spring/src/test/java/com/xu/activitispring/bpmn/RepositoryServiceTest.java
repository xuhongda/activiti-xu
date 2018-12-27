package com.xu.activitispring.bpmn;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.impl.persistence.entity.VariableInstanceEntity;
import org.activiti.engine.repository.*;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author xuhongda on 2018/11/30
 * com.xu.activitispring.bpmn
 * activiti-xu
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
@ContextConfiguration("classpath:config/activiti.context.xml")
public class RepositoryServiceTest {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;


    /**
     * 部署一个流程
     * 需要注意mysql 连接驱动版本
     * <p>
     * 在 act_ge_bytearray 表中会留下部署文件 x.bpmn20.xml 和 x.png 信息
     * ...
     * 在 act_re_xxx  表
     *
     * </p>
     */
    @Test
    public void dy() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();

        Deployment deploy = deploymentBuilder.name("jg")
                .addClasspathResource("bpmn/VacationRequest.bpmn20.xml")
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
     * <p>
     * repositoryService 查询
     * </p>
     */
    @Test
    public void queryRe1() {
        //流程部署对象
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        Deployment deployment = deploymentQuery.deploymentId("17501").singleResult();
        log.info("{}", deployment);
        List<Deployment> list = deploymentQuery.deploymentNameLike("%u%").list();
        //创建本地 Sql 查询
        NativeDeploymentQuery nativeDeploymentQuery = repositoryService.createNativeDeploymentQuery();
        List<Deployment> deployments = nativeDeploymentQuery.sql("select * from act_re_deployment where NAME_ like #{name}")
                .parameter("name", "%u%").listPage(1, 10);
        log.info("部署集合{}", list);
        log.info("{}", deployments);


    }

    @Test
    public void queryRe2() {
        //流程定义对象
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        ProcessDefinition processDefinition = processDefinitionQuery.deploymentId("17501").singleResult();
        log.info("流程定义对象：版本:{}；ID:{};name:{}", processDefinition.getVersion(), processDefinition.getId(), processDefinition.getName());
    }




}
