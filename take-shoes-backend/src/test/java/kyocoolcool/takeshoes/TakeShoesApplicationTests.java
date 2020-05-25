package kyocoolcool.takeshoes;

import kyocoolcool.takeshoes.entity.Book;
import kyocoolcool.takeshoes.entity.Reader;
import kyocoolcool.takeshoes.repository.ReaderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.util.AssertionErrors.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
class TakeShoesApplicationTests {
	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private WebApplicationContext webContext;

	private MockMvc mockMvc;

	@BeforeEach
	public void setupMockMvc() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(webContext)
				.apply(springSecurity())
				.build();
	}

	@Test
	void contextLoads() {
	}

    @Autowired
	private DataSource dataSource;

	@Autowired
	ReaderRepository readerRepository;

	@Test
	public void givenTomcatConnectionPoolInstance_whenCheckedPoolClassName_thenCorrect() {
		assertThat(dataSource.getClass().getName())
				.isEqualTo("org.apache.tomcat.jdbc.pool.DataSource");
	}

	@Test
	public void getReader() {
		System.out.println(readerRepository.findById("chris").orElse(null));
	}

	/*
	 * @description: spring web 集成測試 發送post和request
	 * @return: void
	 * @author: Chris Chen
	 * @time: 2020/5/20 1:46 PM
	 */
	@Test
	public void postBook() throws Exception {
		mockMvc.perform(post("/book/craig")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("title", "BOOK TITLE")
				.param("author", "BOOK AUTHOR")
				.param("isbn", "1234567890")
				.param("description", "DESCRIPTION"))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", "/craig"));
		Book expectedBook = new Book();
		expectedBook.setId(17L);
		expectedBook.setReader("craig");
		expectedBook.setTitle("BOOK TITLE");
		expectedBook.setAuthor("BOOK AUTHOR");
		expectedBook.setIsbn("1234567890");
		expectedBook.setDescription("DESCRIPTION");
		mockMvc.perform(get("/book/craig"))
				.andExpect(status().isOk())
				.andExpect(view().name("readingList"))
				.andExpect(model().attributeExists("books"))
				.andExpect(model().attribute("books", hasSize(1)))
				.andExpect(model().attribute("books", contains(samePropertyValuesAs(expectedBook))));
	}

	/*
	 * @description: 登入轉向測試
	 * @return: void
	 * @author: Chris Chen
	 * @time: 2020/5/20 1:56 PM
	 */
	@Test
	public void homePage_unauthenticatedUser() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location",
						"http://localhost/login"));
	}

//	@Test
//	@WithUserDetails("chris")
	@Test
	@WithMockUser(username="chris", password="123456", roles="READER") //直接繞過對UserDetail正常查詢，用給訂的值創建UserDetail取代
	public void homePage_authenticatedUser() throws Exception {
		Reader expectedReader = new Reader();
		expectedReader.setUsername("chris");
		expectedReader.setPassword("password");
		expectedReader.setFullname("Craig Walls");
		mockMvc.perform(get("/book/chris"))
				.andExpect(status().isOk())
				.andExpect(view().name("readingList"))
				.andExpect(model().attribute("books", hasSize(1)));
	}

	/*
	 * @description:啟動tomcat servlet 容器 測試
	 * @return: void
	 * @author: Chris Chen
	 * @time: 2020/5/20 6:03 PM
	 */
	@Test
	public void pageNotFound() {
		try {
			restTemplate.getForObject(
					"http://localhost:8080/hello", String.class);
			fail("Should result in HTTP 404");
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
			throw e;
		}
	}
}
