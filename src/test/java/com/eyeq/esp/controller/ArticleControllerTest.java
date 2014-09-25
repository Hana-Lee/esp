package com.eyeq.esp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.eyeq.esp.model.Article;
import com.eyeq.esp.model.StudyRoom;
import com.eyeq.esp.model.User;
import com.eyeq.esp.service.StudyRoomManager;
import com.eyeq.esp.service.UserManager;
import com.eyeq.esp.system.config.SpringAppConfig;
import com.eyeq.esp.system.config.SpringWebConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringAppConfig.class, SpringWebConfig.class })
@TransactionConfiguration
@Transactional
public class ArticleControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Autowired
	private StudyRoomManager studyRoomManager;

	@Autowired
	private UserManager userManager;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		createDummyStudyRoom();
	}

	private void createDummyStudyRoom() {
		User owner = userManager.getUser("voyaging");
		if (owner == null) {
			owner = new User();
			owner.setName("이하나");
			owner.setUid("voyaging");
			owner.setEmail("voyaging@naver.com");
			owner.setPassword("dlgksk");
			owner.setEnabled(true);
			owner.setPenaltyScore(0);
			userManager.createUser(owner);
		}
		StudyRoom room = new StudyRoom();
		room.setName("TOGA 설치2");
		room.setStartDate(new Date());
		room.setEndDate(new Date());
		room.setEnabled(true);
		room.setOwner(owner);
		room.addMember(owner);

		studyRoomManager.createStudyRoom(room);

		StudyRoom room2 = new StudyRoom();
		room2.setName("TOGA 설치3");
		room2.setStartDate(new Date());
		room2.setEndDate(new Date());
		room2.setEnabled(false);
		room2.setOwner(owner);

		studyRoomManager.createStudyRoom(room2);
	}

	@Test
	public void editFormTest() throws Exception {
		User user = userManager.getUser("voyaging");
		Article article = new Article();
		this.mockMvc
				.perform(
						get("/study-article/edit-form?studyRoomId=1")
								.sessionAttr("user", user).sessionAttr(
										"article", article))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("studyRoom"));

		// result.getRequest().getAttribute("studyRoom");
	}

	@Test
	public void editFormSubmitTest() throws Exception {
		User user = userManager.getUser("voyaging");

		Article article = new Article();
		article.setTitle("정묵테스트");
		article.setContent("과연 나올까");

		this.mockMvc
				.perform(
						post("/study-article/edit-form-submit?studyRoomId=3")
								.sessionAttr("user", user).sessionAttr(
										"article", article))
				.andExpect(status().isMovedTemporarily())
				.andExpect(redirectedUrl("/study-article/list?studyRoomId=3"));
	}
}
