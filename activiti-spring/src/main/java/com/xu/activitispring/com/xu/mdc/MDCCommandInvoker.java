package com.xu.activitispring.com.xu.mdc;

import org.activiti.engine.impl.agenda.AbstractOperation;
import org.activiti.engine.impl.interceptor.DebugCommandInvoker;
import org.activiti.engine.logging.LogMDC;

/**
 * @author xuhongda on 2018/12/6
 * com.xu.activitispring.com.xu.mdc
 * activiti-xu
 */

public class MDCCommandInvoker extends DebugCommandInvoker {

    @Override
    public void executeOperation(Runnable runnable) {
        //取原来的值是否是生效的
        boolean mdcEnabled = LogMDC.isMDCEnabled();
        LogMDC.setMDCEnabled(true);
        if (runnable instanceof AbstractOperation) {
            AbstractOperation operation = (AbstractOperation) runnable;
            if (operation.getExecution() != null) {
                /*logger.info("Execution tree while executing operation {} :", operation.getClass());
                logger.info("{}", System.lineSeparator() + ExecutionTreeUtil.buildExecutionTree(operation.getExecution()));*/
                //记录数据
                LogMDC.putMDCExecution(operation.getExecution());
            }
        }

        super.executeOperation(runnable);
        //【清理MDC信息】为保证环境的清洁
        LogMDC.clear();
        if (!mdcEnabled) {
            //如果原来的值是不生效的
            //把他的值重新还原一下
            LogMDC.setMDCEnabled(false);

        }
    }


}
