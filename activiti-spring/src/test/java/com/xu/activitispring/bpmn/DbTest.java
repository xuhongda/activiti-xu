package com.xu.activitispring.bpmn;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntityImpl;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;


/**
 * @author xuhongda on 2018/12/18
 * com.xu.activitispring.bpmn
 * activiti-xu
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:config/activiti.context.xml")
@Slf4j
public class DbTest {

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private IdentityService identityService;

    /**
     * 创建表结构
     */
    @Test
    public void test() {
        Map<String, Long> tableCount = managementService.getTableCount();
        Set<String> strings = tableCount.keySet();
        //注意这个{@link org.assertj.core.util.Lists} 对象 ，这个方法还蛮好用
        // ArrayList<String> collect = Streams.stream(strings).collect(toCollection(ArrayList::new));
        // Streams.stream(strings).collect(toCollection(()->new ArrayList<>()));
        ArrayList<String> stringList = Lists.newArrayList(strings);
        //排序
        Collections.sort(stringList);
        stringList.forEach(a -> log.info("{}", a));
    }

    /**
     * 删除表结构
     */
    @Test
    public void dropTable() {
        managementService.executeCommand(a -> {
            a.getDbSqlSession().dbSchemaDrop();
            return null;
        });
    }

    /**
     * 通过字节流往表里插入数据
     */
    @Test
    public void testByteArray() {
        managementService.executeCommand(a -> {
            ByteArrayEntityImpl entity = new ByteArrayEntityImpl();
            entity.setName("test");
            entity.setBytes("test byte".getBytes());
            a.getByteArrayEntityManager().insert(entity);
            return null;
        });
    }

    /**
     * 用户与用户组
     */
    @Test
    public void testIdentity() {
        User user1 = identityService.newUser("user1");
        user1.setFirstName("xu");
        user1.setLastName("xx");
        user1.setEmail("xx@xx.com");
        identityService.saveUser(user1);
        User user2 = identityService.newUser("user2");
        identityService.saveUser(user2);
        Group group1 = identityService.newGroup("group1");
        group1.setName("for test");
        identityService.saveGroup(group1);
        //创建 Group 与 user 的关系
        identityService.createMembership(user1.getId(), group1.getId());
        identityService.createMembership(user2.getId(), group1.getId());

        identityService.setUserInfo(user1.getId(), "age", "18");
    }
}
