package kyocoolcool.takeshoes.repository;

import kyocoolcool.takeshoes.entity.Book;
import kyocoolcool.takeshoes.entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Chris Chen
 * @version 1.0
 * @className ReaderRepository
 * @description
 * @date 2020/5/8 5:05 PM
 **/
@Repository
public interface ReaderRepository extends JpaRepository<Reader, String> {
    List<Reader> findByUsername(String userName);
}
