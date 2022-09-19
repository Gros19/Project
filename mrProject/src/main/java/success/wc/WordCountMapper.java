package success.wc;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
//앞에 두개는 레코드 리더로부터 전달받는.... 맵함수가 처리하는 부분
//0~23 the cat ...
//<Long-LongWritable, String>
// 맵 리듀스에서 파일 단위로 분석한다!-> 무조건 fileInputFormat
//레코드 리더가 키-값 구조로 받기 때문


//맵 -> 셔플 엔 소트
//키와 값 데이터 형식
//<Text-String, Integer>
//the cat set... [1, 1, 1], [1],[1]
/*
*
* */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {


    @Override  // 키는 Long 값은 Text
    protected void map(LongWritable key, Text value,Context context) throws IOException, InterruptedException {


        //분석할 한줄한줄한줄....
        String line = value.toString();


        //\W+ 공백단위로 잘라
        for(String word : line.split("\\W+")){
            if(word.length() > 0){
                //shuffle and sort로 데이터 전달
                //하둡에서 쓰는 문자 Text()로 변경
                //하둡에서 쓰는 숫자 IntWritable()로 변경
                context.write(new Text(word), new IntWritable(1));
            }
        }
    }
}
