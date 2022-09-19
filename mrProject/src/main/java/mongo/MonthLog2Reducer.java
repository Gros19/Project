package mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.log4j.Log4j;
import mongo.conn.MongoDBConnection;
import mongo.dto.AccessLogDTO;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 리듀스 역할을 수행하기 위해 Reducer 자바 파일을 상속받아야 함
 * Reducer 파일의 앞에 2개 데이터 타입(Text, Text)는 Shuffle and Sort로 보낸 데이터 타입과 동일
 * 보통 Mapper에서 보낸 데이터 타입과 동일함
 * Reducer 파일의 뒤의 2개 데이터 타입(Text, IntWritable)은 결과 파일을 생성에 사용될 키와 값*/
@Log4j
public class MonthLog2Reducer extends Reducer<Text, Text, Text, IntWritable> {
    /*mongoDB 객체*/
    private MongoDatabase mongodb;

    private Map<String, String> months = new HashMap<>();
    public MonthLog2Reducer(){
        /*생성자에 매핑 정보 저장*/
        this.months.put("Jan", "LOG_01");
        this.months.put("Feb", "LOG_02");
        this.months.put("Mar", "LOG_03");
        this.months.put("Apr", "LOG_04");
        this.months.put("May", "LOG_05");
        this.months.put("Jun", "LOG_06");
        this.months.put("Jul", "LOG_07");
        this.months.put("Aug", "LOG_08");
        this.months.put("Sep", "LOG_09");
        this.months.put("Oct", "LOG_10");
        this.months.put("Nov", "LOG_11");
        this.months.put("Dec", "LOG_12");
    }


    @Override
    protected void setup(Reducer<Text, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
        /*MongoDB 객체 생성을 통해 접속*/
        this.mongodb = new MongoDBConnection().getMongoDB();

        /*1월부터 12월까지 컬렉션 접속
        * this.months 변수에 저장된 데이터 수만큼 반복*/
        for(String month : this.months.keySet()){
            /*this.months 변수로 생성될 컬렉션 이름 가져오기*/
            /*컬렉션 이름 : LOG_01 ~ LOG_12*/
            String colNm = this.months.get(month);

            /*리듀스 객체는 12개가 생성되기 때문에 setup()함수는 리듀서 객체당 1번씩 총 12번 실행됨
            * 이미 생성된 컬렉션은 다시 생성되면 안돼
            * 컬렉션 생성할지 결정할 변수(true : 생성 / false : 미생성)*/
            boolean create = true;

            /*컬렉션이 이미 존재하는지 체크
            * String-data-mongo는 컬렉션 존재여부 체크 함수를 제공하지만,
            * MongoDriver 라이브러리는 제공하지 않아, 컬렉션 존재 여부 체크 함수를 만들어 사용해야 */
            for(String s: this.mongodb.listCollectionNames()){
                /*컬렉션 존재하면 생성하지 못하도록 create변수를 false로 변경함*/
                if(colNm.equals(s)){
                    create = false;
                    break;
                }
            }
            /*컬렉션이 생성 안 됐으면 생성!*/
            if(create){
                /*컬렉션 생성*/
                this.mongodb.createCollection(colNm);
            }
        }

    }

    /*부모 Reducer 자바 파일에 작성된 reduce 함수를 덮어쓰기 수행
    * reduce 함수는 Shuffle and Sort로 처리된 데이터마다 실행됨
    * 처리된 데이터의 수가 500개라면, reduce 함수 500번 실행됨
    *
    * Reducer 객체는 기본값이 1개로 1개의 쓰레드로 처리함*/
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        List<Document> pList = new ArrayList<>();

        /*월에 따른 컬렉션 이름 가져오기*/
        String colNm = this.months.get(key.toString());

        log.info("key : " + key);
        log.info("colNm : " + colNm);
        /*저장을 위한 컬렉션 정보 가져오기*/
        MongoCollection<Document> col = mongodb.getCollection(colNm);

        /*
        * Shuffle and Sort로 인해 단어별고 데이터들의 값들이 List 구조로 저장됨
        * 파티셔너를 통해 같은 월에 해당하는 JSON 문자열만 넘어옴
        * Mon : {'JSON 문자열', 'JSON 문자열' , 'JSON 문자열', 'JSON 문자열'}*/
        for(Text value : values){
            /*Text 타입의 JSON 문자열을 String 타입으로 변경하기*/
            String json = value.toString();

            /*JSON 문자열을 DTO의 변수마다 값 넣어주기
            * JSON -> DTO*/
            AccessLogDTO pDTO = new ObjectMapper().readValue(json, AccessLogDTO.class);

            /*DTO 데이터를 MongoDB에 저장 가능한 Document로 변환하기
            DTO > Document*/
            Document doc = new Document(new ObjectMapper().convertValue(pDTO, Map.class));

            /*서버 메모리를 믿고, 한번에 저장하기 위해 저장할 데이터를 List에 넣기*/
            pList.add(doc);

            doc = null;
        }

        /*mongoDB에 한번에 저장될 수 있는 메모리 크기 제한이 있어 ArrayList에 많은 데이터를 한번에 저장하기*/
        /*저장된 전체 레코드 수*/
        int pListSize = pList.size();
        /*한번에 저장할 레코드 수를 50000개로 설정함*/
        int blockSize = 50000;

        for(int i = 0; i < pListSize; i += blockSize){
            col.insertMany(new ArrayList<>(pList.subList(i, Math.min(i + blockSize, pListSize))));
        }
        col = null;
        pList = null;
    }

    @Override
    protected void cleanup(Reducer<Text, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {

        /*MongoDB 접속 해제*/
        this.mongodb = null;
    }
}
