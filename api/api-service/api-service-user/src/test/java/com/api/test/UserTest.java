package com.api.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.data.pojo.select.Filter;
import com.api.data.user.pojo.entity.UserEntity;
import com.api.data.user.repository.UserRepository;
import com.api.main.ApiServiceUserApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiServiceUserApplication.class)
public class UserTest {

	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void findOne() {
//		UserEntity user = userRepository.findOne("402881e466e1c20d0166e1c533300000");
//		TemporalAccessor accessor = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").parse("2018-11-05 10:48:09");
//		UserEntity user = userRepository.findOne(Filter.gt("createDate", Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())));
//		LocalDateTime localDateTime = LocalDateTime.from(accessor);
		UserEntity user = userRepository.findOne(Filter.eq("id", "402881e466e1c20d0166e1c533300000"));
		System.out.println(user.getName());
	}

}
