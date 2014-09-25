package com.eyeq.esp.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.eyeq.esp.service.ImageManager;
import com.eyeq.esp.system.config.SpringAppConfig;
import com.eyeq.esp.system.config.SpringWebConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringAppConfig.class, SpringWebConfig.class })
@TransactionConfiguration
@Transactional
public class ImageModelTest {

	@Autowired
	private ImageManager imageManager;

	@Test
	public void testCreateImage() {
		Image img = new Image();
		img.setName("hanalee_" + System.currentTimeMillis() + ".png");
		img.setRealName("HanaLee.png");
		img.setSize(123123123L);

		imageManager.createImage(img);

		img.setUrl("/uploaded/images/" + img.getId());
		System.out.println(img.getId());
	}

}
