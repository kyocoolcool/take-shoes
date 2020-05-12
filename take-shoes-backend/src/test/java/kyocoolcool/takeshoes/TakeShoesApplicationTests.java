package kyocoolcool.takeshoes;

import kyocoolcool.takeshoes.repository.ReaderRepository;
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
}
