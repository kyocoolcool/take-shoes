package kyocoolcool.takeshoes.repository;

import kyocoolcool.takeshoes.entity.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Chris Chen
 * @version 1.0
 * @className PersonRepository
 * @description
 * @date 2020/5/6 11:11 AM
 **/
@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
}
