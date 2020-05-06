package kyocoolcool.takeshoes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TakeShoesApplicationTests {

	@Test
	void contextLoads() {
	}

    @Autowired
	private DataSource dataSource;

	@Test
	public void givenTomcatConnectionPoolInstance_whenCheckedPoolClassName_thenCorrect() {
		assertThat(dataSource.getClass().getName())
				.isEqualTo("org.apache.tomcat.jdbc.pool.DataSource");
	}
}
