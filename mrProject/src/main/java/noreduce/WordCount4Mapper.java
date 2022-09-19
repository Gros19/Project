package noreduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;



public class WordCount4Mapper extends Mapper<LongWritable, Text, Text, LongWritable> {
    @Override
    protected void map(LongWritable key, Text value,Context context) throws IOException, InterruptedException {
        //분석할 한줄한줄한줄....
        String line = value.toString();
        //\W+ 공백단위로 잘라
        for(String word : line.split("\\W+")){
            if(word.length() > 0){
                //shuffle and sort로 데이터 전달
                //하둡에서 쓰는 문자 Text()로 변경
                //하둡에서 쓰는 숫자 IntWritable()로 변경
                context.write(new Text(word), new LongWritable(1));
            }
        }
    }
}


/*
package noreduce;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordCount4Mapper extends Mapper<LongWritable, Text, Text, LongWritable> {

@Override
public void map (LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
    // 분석할 파일의 한 줄 값
    String line = value.toString(); // 데이터타입 string로 변경
    //단어 빈도수 구현은 공백을 기준으로 단어로 구분함
    // 분석할 한 줄 내용을 고앱ㄱ으로 나눔
    // word 변수는 공백을 나워진 단어가 들어감
    for (String word : line.split("\\W+")) {

        // word 변수에 값이 있다면 ..
        if(word.length() > 0) {

            // suffle and Sort로 데이터를 전달하기
            // 전달하는 값은 단어와 빈도스(1)를 전달함
            context.write(new Text(word),new LongWritable(1));
        }
    }
}
}
 */