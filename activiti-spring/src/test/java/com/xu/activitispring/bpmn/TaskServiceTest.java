package com.xu.activitispring.bpmn;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.VariableInstanceEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
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
 * @author xuhongda on 2018/12/27
 * com.xu.activitispring.bpmn
 * activiti-xu
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:config/activiti.context.xml")
@Slf4j
public class TaskServiceTest {


    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ManagementService managementService;


    /**
     * 查询出所欲任务
     */
    @Test
    public void getAll() {
        List<Task> tasks = taskService.createTaskQuery().list();
        log.info("{}", tasks.size());
    }

    @Test
    public void test2() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        log.info("ProcessDefinition——id:{}", list.get(1).getId());
        List<Task> tasks = taskService.createTaskQuery().processDefinitionId(list.get(1).getId()).list();
        tasks.forEach(t -> log.info(t.getName(), t.getAssignee(), t.getExecutionId()));

    }


    /**
     * 查看任务
     */
    @Test
    public void task() {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        // List<Task> list = taskService.createTaskQuery().list();
        for (Task task : tasks) {
            log.info("Task available: " + task.getName());
        }
        log.info("任务数量{}", tasks.size());
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


    /**
     * 执行审批
     */
    @Test
    public void complete() {

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
