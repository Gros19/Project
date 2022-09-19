package success;

import lombok.extern.log4j.Log4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

@Log4j
public class ResultCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    //맵 리듀스 잡 이름
    String appName = "";

    //URL 전송 성공 여부 코드값
    //성공 : 200 / 실패 : !200
    String resultCode = "";

    @Override
    protected void setup(Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {

        //사용자 정보 가져오기
        Configuration conf = context.getConfiguration();

        //드라이버에서 정의된 맵리듀스 잡 이름 가져오기
        this.appName = conf.get("AppName");

        //드라이버에서 정의된 환경설정값 가져오기
        //없으면 200으로 설정

        this.resultCode = conf.get("resultCode", "200");

        log.info("[" + this.appName + "] 난 map 함수를 실행하기 전에 1번만 실행되는 setup합수이다." );
    }

    @Override
    protected void cleanup(Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
        log.info("[" + this.appName + "] 난 map 함수를 실행한 후에 1번만 실행되는 cleanup합수이다." );
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        //분석할 파일의 한 줄 값
        String line = value.toString();

        //단어별로 나누기
        String[] arr = line.split("\\W+");

        //전송 결과 코드가 존재하는 위치
        //로그의 마지막에서 2번째에 성공코드 값이 존재함
        int pos = arr.length -2;

        //전송 결과 코드
        String result = arr[pos];

        log.info("[" + this.appName + "] " + resultCode);

        //드라이버 파일에서 정의한 코드값과 로그의 코드의 코드값이 일치한다면
        if ( resultCode.equals(result)){
            context.write(new Text(result), new IntWritable(1));
        }
    }
}
