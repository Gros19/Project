package mongo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class AccessLogDTO {
    /*IP*/
    private String ip;
    /*요청일시*/
    private String reqTime;
    /*요청방법*/
    private String reqMethod;
    /*요청URI*/
    private String reqURI;
}
