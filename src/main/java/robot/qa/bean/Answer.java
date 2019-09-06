package robot.qa.bean;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    @Id
    private ObjectId id;
    /** 关键字 */
    private String keyword;
    /** 回答内容 */
    private String value;
    /** 时间 */
    private Date createTime;

}
