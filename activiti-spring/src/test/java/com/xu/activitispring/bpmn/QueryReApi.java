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
public class QueryReApi {

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

    @Test
    public void query() {
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee("kermit")
                .processVariableValueEquals("orderId", "0815")
                .orderByTaskDueDate().asc()
                .list();
        log.info("任务列表{}", tasks);
    }

    @Test
    public void queryBySql() {
        List<Task> tasks = taskService.createNativeTaskQuery()
                .sql("SELECT count(*) FROM " + managementService.getTableName(Task.class) + " T WHERE T.NAME_ = #{taskName}")
                .parameter("taskName", "gonzoTask")
                .list();
        log.info("任务列表{}", tasks);
        long count = taskService.createNativeTaskQuery()
                .sql("SELECT count(*) FROM " + managementService.getTableName(Task.class) + " T1, "
                        + managementService.getTableName(VariableInstanceEntity.class) + " V1 WHERE V1.TASK_ID_ = T1.ID_")
                .count();
        log.info("{}", count);
    }
}
