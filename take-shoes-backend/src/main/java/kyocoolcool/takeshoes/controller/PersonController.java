package kyocoolcool.takeshoes.controller;

import kyocoolcool.takeshoes.entity.Person;
import kyocoolcool.takeshoes.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chris Chen
 * @version 1.0
 * @className PersonController
 * @description
 * @date 2020/5/6 11:09 AM
 **/

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonRepository personRepository;

    @RequestMapping("/add")
    public Object add(@RequestParam(name = "name", required = true) String name) {
        Person person = new Person();
        person.setName(name);
        personRepository.save(person);
        return person;
    }

    @RequestMapping("/listPerson")
    public Object list() {
        Iterable<Person> users = personRepository.findAll();
        return users;
    }
}
