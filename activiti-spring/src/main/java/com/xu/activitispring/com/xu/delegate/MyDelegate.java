package com.xu.activitispring.com.xu.delegate;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @author xuhongda on 2018/12/6
 * com.xu.activitispring.com.xu.delegate
 * activiti-xu
 */
@Slf4j
public class MyDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        log.info("异常。。。");
        throw new RuntimeException("");
    }
}
