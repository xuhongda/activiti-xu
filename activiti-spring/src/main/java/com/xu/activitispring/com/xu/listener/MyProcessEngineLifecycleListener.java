package com.xu.activitispring.com.xu.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineLifecycleListener;


/**
 * <p>
 * 流程引擎监听类
 * </p>
 *
 * @author xuhongda on 2018/12/12
 * com.xu.activitispring.com.xu.listener
 * activiti-xu
 */
@Slf4j
public class MyProcessEngineLifecycleListener implements ProcessEngineLifecycleListener {
    /**
     * Called right after the process-engine has been built.
     *
     * @param processEngine engine that was built
     */
    @Override
    public void onProcessEngineBuilt(ProcessEngine processEngine) {
        log.info("流程引擎开启{}", processEngine.toString());
    }

    /**
     * Called right after the process-engine has been closed.
     *
     * @param processEngine engine that was closed
     */
    @Override
    public void onProcessEngineClosed(ProcessEngine processEngine) {
        log.info("流程引擎关闭{}", processEngine.toString());
    }
}
