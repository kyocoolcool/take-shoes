package kyocoolcool.takeshoes.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Chris Chen
 * @version 1.0
 * @className Person
 * @description
 * @date 2020/5/6 11:05 AM
 **/

@Entity
@Table(name = "person") // 指定关联的数据库的表名
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 主键ID

    private String name; // 姓名

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + "]";
    }

}
