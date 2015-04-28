package resume.application.model;

import javax.persistence.*;

import com.sun.istack.internal.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.search.annotations.*;

import java.sql.Timestamp;

/**
 * Модельный класс представляющий резюме с сайта.
 */
@Getter
@Setter
@Entity
@Indexed
@Table
@JsonIgnoreProperties({ "id", "updateDate"})
public class Resume {

    public Resume() {

    }

    public Resume(long id, String title, Timestamp updateDate, String city, String description, Integer salary) {
        this.id = id;
        this.title = title;
        this.updateDate = updateDate;
        this.city = city;
        this.description = description;
        this.salary = salary;
    }

    @Id
    @NotNull
    private long id;

    @Field
    @NotNull
    private String title;

    @Field
    @NotNull
    @DateBridge(resolution = Resolution.MILLISECOND)
    private Timestamp updateDate;

    @Field
    @NotNull
    private String city;

    @Field
    @Column(length = Integer.MAX_VALUE)
    private String description;

    @Field
    private Integer salary;
}
