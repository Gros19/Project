package mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import mongo.dto.AccessLogDTO;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Log4j
public class MonthLog2Mapper extends Mapper<LongWritable, Text, Text, Text> {
    List<String> months = null;
    public MonthLog2Mapper(){
        this.months = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov","Dec");
    }
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        /*분석할 파일 한 줄 값*/
        String[] fields = value.toString().split(" ");

        /*ip 추출*/
        String ip = fields[0];
        /*요청일시*/
        String reqTime ="";

        /*일부 데이터가 요청일시 값이 누락된 경우가 있어 요청일시 값이 존재하는지 확인*/
        if(fields[3].length() > 2){
            /*요청일시 추출*/
            reqTime = fields[3].substring(1);
        }
        /*GET, POST 요청 방법 추출*/
        String reqMethod = "";

        /*일부 데이터가 요청방법이 누락된 경우가 있어 요청방법이 존재하는지 확인*/
        if(fields[5].length() > 2){
            /*요청방법 추출*/
            reqMethod = fields[5].substring(1);
        }

        /*요청 URI 추출*/
        String reqURI = fields[6];

        String reqMonth = "";
        String[] dtFields = fields[3].split("/");
        /*요정일시에서 월 정보 추출하기*/
        if (dtFields.length > 1){
            reqMonth = dtFields[1];
        }

        log.info("ip : " + ip);
        log.info("reqTime : " + reqTime);
        log.info("regMethod : " +reqMethod);
        log.info("reqURI : " + reqURI);


        /*추출한 정보를 저장하기 위해 pDTO 선언 후, 값 저장
         * IP는 키로 사용하기 때문에 DTO에 저장하지 않음
         * */
        AccessLogDTO pDTO = new AccessLogDTO();
        pDTO.setIp(ip);
        pDTO.setReqMethod(reqMethod);
        pDTO.setReqTime(reqTime);
        pDTO.setReqURI(reqURI);


        /*DTO에 저장된 내요을 JSON 문자열로 변경
        * DTO -> JSON 변경*/
        String json = new ObjectMapper().writeValueAsString(pDTO);

        /*월 정보가 일치하는지 체크*/
        if(months.contains(reqMonth)){

            /*MonthLog2Partitioner로 보내서, 월별로 리듀스 분할하기*/
            context.write(new Text(reqMonth), new Text(json));
        }

    }
}