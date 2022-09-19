package partition;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;


/**
 * 리듀스 역할을 수행하기 위해 Reducer 자바 파일을 상속받아야 함
 * Reducer 파일의 앞에 2개 데이터 타입(Text, Text)는 Shuffle and Sort로 보낸 데이터 타입과 동일
 * 보통 Mapper에서 보낸 데이터 타입과 동일함
 * Reducer 파일의 뒤의 2개 데이터 타입(Text, IntWritable)은 결과 파일을 생성에 사용될 키와 값*/
public class TimeLogReducer extends Reducer<Text, Text, Text, IntWritable> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        /*IP별 빈도수를 계산하기 위한 변수*/
        int ipCount = 0;

        /*Shuffle and Sort로 인해 단어별로 데이터들의 값들이 List 구조로 저장됨
        * 파티셔너를 통해 같은 월에 해당하는 IP만 넘어옴
        * 192.168.0.1 : {'01','01','01','01'}
        * 배열의 수는 IP가 호출된 수이며 배열의 수를 계산*/
        for (Text value : values){
            ipCount++;
        }

        /*분석 결과 파일에 데이터 저장하기*/
        context.write(key, new IntWritable(ipCount));
    }
}
