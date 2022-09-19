package cc;

//매퍼에서 셔플 에[ㄴ 소트로 보내는 타입

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

//결과 파일 생성할 때 키-값으로 지정
public class CharCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    @Override //<String, 횟수를 합치기 위해 리스트 형식으로>
    protected void reduce(Text key, Iterable<IntWritable> values,  Context context) throws IOException, InterruptedException {


        //단어별 빈도수를 계산하기 위한 변수
        int wordCount = 0;



        //Suffle and Sort로 인해 단어별로 데이터들을 값들이 List 구조로 저장됨
        //이협건 : {1, 1, 1, 1, 1}
        //모든 값은 1이게 모두 더하기 해도 됨
        for(IntWritable value : values){
            //값을 모두 더하기
            wordCount += value.get();
        }


        //리듀스에서 context는 파일을 생성성
        context.write(key, new IntWritable(wordCount));
    }
}
