package kyocoolcool.takeshoes;

import kyocoolcool.takeshoes.entity.Book;
import kyocoolcool.takeshoes.entity.Reader;
import kyocoolcool.takeshoes.repository.ReaderRepository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TakeShoesApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private WebApplicationContext webContext;

    @Value("${local.server.port}")
    private int port;

    private MockMvc mockMvc;

    private static ChromeDriver browser;


    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    public void openBrowser() {
        browser = new ChromeDriver();
        browser.manage().timeouts()
                .implicitlyWait(10, TimeUnit.SECONDS);
    }
//
//    @AfterEach
//    public void closeBrowser() {
//        browser.quit();
//    }


    @Test
    public void addBookToEmptyList() throws MalformedURLException {
		final String os = System.getProperty("os.name").toLowerCase();
		String baseUrl = "http://localhost:" + port+"/book/chris";
        browser.get(baseUrl);
        assertEquals("git by chris (ISBN: 123456789)\n" +
                "git introduction", browser.findElementByTagName("div").getText());
        browser.findElementByName("title").sendKeys("BOOK TITLE");
        browser.findElementByName("author").sendKeys("BOOK AUTHOR");
        browser.findElementByName("isbn").sendKeys("1234567890");
        browser.findElementByName("description").sendKeys("DESCRIPTION");
        browser.findElementByTagName("form").submit();
//        WebElement dl = browser.findElementByCssSelector("dt.bookHeadline");
//        assertEquals("BOOK TITLE by BOOK AUTHOR (ISBN: 1234567890)", dl.getText());
//        WebElement dt = browser.findElementByCssSelector("dd.bookDescription");
//        assertEquals("DESCRIPTION", dt.getText());
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
    @WithMockUser(username = "chris", password = "123456", roles = "READER") //直接繞過對UserDetail正常查詢，用給訂的值創建UserDetail取代
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
