package com.demo;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"mysql"}) 
public class MainApplicationTests {
	
	String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzdWJqZWN0IiwiaWF0IjoxNTE2MjM5MDIyfQ.eB2c9xtg5wcCZxZ-o-sH4Mx1JGkqAZwH4_WS0UcDbj_nen0NPBj6CqOEPhr_LZDagb4mM6HoAPJywWWG8b_Ylnn5r2gWDzib2mb0kxIuAjnvVBrpzusw4ItTVvP_srv2DrwcisKYiKqU5X_3ka7MSVvKtswdLY3RXeCJ_S2W9go";

	
	@Autowired private MockMvc mockMvc;
	
	@Test
	public void returnsPage() throws Exception {
	    mockMvc
	        .perform(get("/"))
	        .andExpect(status().isOk())
	        .andExpect(view().name("index")); // return jsp name
	  }

	@Test
	public void returnsHelloWorld() throws Exception {
	    mockMvc
	        .perform(get("/kafka/"))
	        .andExpect(status().isOk())
	        .andExpect(content().string(containsString("Hello World!")));
	  }
	
	@Test
	public void returnsApi() throws Exception {
	    mockMvc
	        .perform(get("/api/tutorials").with(bearerToken(this.token)))
	        .andExpect(status().isOk());
	  }
	
	private static BearerTokenRequestPostProcessor bearerToken(String token) {
		return new BearerTokenRequestPostProcessor(token);
	}

	private static class BearerTokenRequestPostProcessor implements RequestPostProcessor {

		private String token;

		BearerTokenRequestPostProcessor(String token) {
			this.token = token;
		}

		@Override
		public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
			request.addHeader("Authorization", "Bearer " + this.token);
			return request;
		}

	}

}
