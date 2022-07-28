package org.jarvis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class JarvisApplicationTests {

	@Autowired
	MockMvc mockMvc;

//	@Test
//	void contextLoads() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.get("/"))
//				.andDo(MockMvcResultHandlers.print())
//				.andExpect(MockMvcResultMatchers.jsonPath());
//	}

}
