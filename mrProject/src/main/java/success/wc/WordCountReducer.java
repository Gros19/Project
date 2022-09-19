package success.wc;


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

//결과 파일 생성할 때 키-값으로 지정
public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    @Override //<String, 횟수를 합치기 위해 리스트 형식으로>
    protected void reduce(Text key, Iterable<IntWritable> values,  Context context) throws IOException, InterruptedException {


        //단어
        int wordCount = 0;



        //Suffle and Sort로 인해 단어별로 데이터들을 값들이 List 구조로 저장됨
        //이협건 :
        //모든 값은 1이게 모두 더하기 해도 됨
        for(IntWritable value : values){
            //값을 모두 더하기
            wordCount += value.get();
        }


        //리듀스에서 context는 파일을 생성성
        context.write(key, new IntWritable(wordCount));
    }
}
