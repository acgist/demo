package com.acgist.activiti;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IdentityTest {

	@Autowired
	private IdentityService identityService;
	
	@Test
	public void testUser() {
		Group group = this.identityService.newGroup("manager");
		this.identityService.saveGroup(group);
		User user = this.identityService.newUser("acgist");
		this.identityService.saveUser(user);
		this.identityService.createMembership("acgist", "manager");
	}
	
	@Test
	public void testDelete() {
		this.identityService.createUserQuery().list().forEach(user -> this.identityService.deleteUser(user.getId()));
		this.identityService.createGroupQuery().list().forEach(group -> this.identityService.deleteGroup(group.getId()));
	}
	
}
