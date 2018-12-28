package com.xu.activitispring.bpmn;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.repository.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
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
     * 一个流程可以多次部署,版本号会发生增加，当启动流程时会启动最大的版本号版本
     * <p>
     * 在 act_ge_bytearray 表中会留下部署文件 x.bpmn20.xml 和 x.png 信息
     * ...
     * 在 act_re_xxx  表
     * </p>
     */
    @Test
    public void dy() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();

        Deployment deploy = deploymentBuilder.name("VacationRequest")
                .key("VacationRequest")
                .addClasspathResource("bpmn/VacationRequest.bpmn20.xml")
                .deploy();
        log.info("流程部署对象:{}", deploy);
        log.info("Number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());
    }

    /**
     * 根据inputStream输入流部署
     *
     * @throws Exception
     */
    @Test
    public void inputStreamFromAbsolutePathTest() throws Exception {
        Resource resource = new ClassPathResource("bpmn/myOne.bpmn20.xml");
        if (resource.exists()) {
            InputStream inputStream = resource.getInputStream();
            repositoryService.createDeployment().addInputStream("myOne.bpmn20.xml", inputStream).deploy();
            //验证部署是否成功
            ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
            long count = processDefinitionQuery.processDefinitionKey("my_activiti_one").count();
            log.info("key 为my_activiti_one 的流程部署次数：{}", count);
        }
    }

    //TODO: 根据 zip 包部署

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


    /**
     * 删除已部署的流程
     */
    @Test
    public void delete() {
        List<Deployment> deployments = repositoryService.createDeploymentQuery().deploymentKey("VacationRequest").list();
        log.info("数量={},", deployments.size());
        deployments.forEach(d -> log.info("流程部署信息={}", ToStringBuilder.reflectionToString(d, ToStringStyle.JSON_STYLE)));
        //删除key重复的流程
        if (deployments.size() > 1) {
            for (int i = 0; i < deployments.size() - 1; i++) {
                repositoryService.deleteDeployment(deployments.get(i).getId(), true);
            }
        }

    }

}
