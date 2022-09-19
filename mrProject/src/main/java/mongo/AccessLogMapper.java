package mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.log4j.Log4j;
import mongo.conn.MongoDBConnection;
import mongo.dto.AccessLogDTO;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.Document;

import java.io.IOException;
import java.util.Map;

@Log4j
public class AccessLogMapper extends Mapper<LongWritable, Text, Text, Text> {
    /*MongoDB 객체*/
    private MongoDatabase mongodb;
    /*저장될 MongoDB 컬렉션명*/
    private String colNm = "ACCESS_LOG";

    @Override
    protected void setup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        /*MongoDB 객체 생성을 통해 접속*/
        this.mongodb = new MongoDBConnection().getMongoDB();

        /*컬렉션을 생성할지 결정할 변수(true : 생성/ false : 미생성)*/
        boolean create = true;

        /*컬렉션이 존재하는지 체크
         * Spring-data-mongo는 컬렉션 존재 여부 체크 함수를 제공하지만
         * MongoDriver 라이브러리는 제공하지 않아, 컬렉션 존재 여부 체크 함수를 만들어 사용해야 함
         * */
        for (String s : this.mongodb.listCollectionNames()) {
            /*컬렉션이 존재하면 생성하지 못하도록 create 변수를 false를 변경함*/
            if (this.colNm.equals(s)) {
                create = false;
                break;
            }
        }

        if (create) {
            this.mongodb.createCollection(this.colNm);
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {

        /*분석할 파일의 한줄값*/
        String[] fields = value.toString().split(" ");

        /*IP 추출*/
        String ip = fields[0];

        /*요청일시*/
        String reqTime = "";
        /*일부 데이터가 요청일시 값이 누락된 경우가 있어 요청일시 값이 존재하는지 체크*/
        if(fields[3].length() > 2){
            /*요청일시 추출*/
            reqTime = fields[3].substring(1);
        }
        /*GET, POST 요청 방식 추출*/
        String reqMethod = "";

        /*일부 데이터가 요청일시 값이 누락된 경우가 있어 요청일시 값이 존재하는지 체크*/
        if (fields[5].length() > 2){
            /*요청일시 추출*/
            reqMethod = fields[5].substring(1);
        }
        /*요청 URI 추출*/
        String reqURI = fields[6];

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

        /*저장할 MongoDB 컬렉션 가져오기*/
        MongoCollection<Document> col = this.mongodb.getCollection(this.colNm);

        /*MongoDB에 저장하기*/
        col.insertOne(new Document(new ObjectMapper().convertValue(pDTO, Map.class)));

        col = null;
    }

    @Override
    protected void cleanup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        /*mongoDB 접속 해제*/
        this.mongodb = null;

    }
}
