package com.xu.activitispring.bpmn;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xuhongda on 2018/12/12
 * com.xu.activitispring.bpmn
 * activiti-xu
 */
@Slf4j
/*@ContextConfiguration(locations = {"classpath:config/activiti.context.xml"})*/
@SpringBootTest
@RunWith(SpringRunner.class)
public class CreateProcessEngine {

    /**
     * 编程方式创建流程引擎
     */
    @Test
    public void cr() {

        ProcessEngineConfiguration standaloneProcessEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        standaloneProcessEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
        standaloneProcessEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/activitidemo?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT");
        standaloneProcessEngineConfiguration.setJdbcUsername("root");
        standaloneProcessEngineConfiguration.setJdbcPassword("123456");
        standaloneProcessEngineConfiguration.setDatabaseSchemaUpdate("true");
        ProcessEngine processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        log.info("{}", processEngine);
    }

    @Test
    public void info() {
        //创建基于内存数据库的流程引擎对象
        ProcessEngineConfiguration standaloneInMemProcessEngineConfiguration = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
        ProcessEngine processEngine = standaloneInMemProcessEngineConfiguration.buildProcessEngine();
        String name = processEngine.getName();
        String version = ProcessEngine.VERSION;
        log.info("流程引擎名称{}，流程引擎模板{}", name, version);
        //关闭流程引擎
        processEngine.close();
    }
}
