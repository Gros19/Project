package ip;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class IPCount {
        //맵 리듀스 실행 함수
    /*
    @param1
    분석할 대상
    @param1
    분석한 결과 word count 연산된 결과 폴더
    */
        public static void main(String[] args) throws Exception{
            //파라미터는 분석할 파일(폴더)과 분석 결과가 저장될 파일(폴더)로 2개를 받음
            if(args.length != 2){
                System.out.printf("분석할 폴더(파일) 및 분석결과각 저장될 폴더를 입력해야 합니다..");;
                System.exit(-1);
            }

            //리눅스 서버에서 실행되는 맵리듀스 객체를 가져오고
            //맵리듀스 실행을 위한 잡 객체를 가져오기
            //하둡이 실행되면, 기본적으로 잡 객체를 메모리에 올림
            Job job = Job.getInstance();

            //맵리듀스 관리 파일
            //this.clsaa 안 돼
            //맵리듀스 잡이 시작되는 main함수가 존재하는 파일 설정
            job.setJarByClass(IPCount.class);

            //옵션인데 로그찍어서 볼 때 확인용
            //맵리듀스 잡 이름 설정, 리소스 매니저 등 맵리듀스 실행 결과 및 로그 확인할 떄 편리함
            job.setJobName("###Char Count###");

            //레코드리더가 한줄씩 읽어 그 레코드리더 역할을 해주는게 레코드리더-FileInputFormat...setInputPaths()
            //분석할 폴더(파일)-- 첫번째 파라미터
            FileInputFormat.setInputPaths(job, new Path(args[0]));

            //맵리듀스로부터 생성된 결과를 파일로 생성할 때
            //분석 결과가 저장되는 폴더
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            //맵퍼로 수행할 자바 파일 설정
            job.setMapperClass(IPCountMapper.class);

            //리듀스 역할을 할 파일 설정
            job.setReducerClass(IPCountReducer.class);
            //맵리듀스에서 데이터를 전송할 때 키-값으로 shuffle and sort
            //리듀서에서 저장되는 파일의 데이터
            job.setOutputKeyClass(Text.class);
            //그때 타입은 명확히 해야 함
            //키는 Text.class == String-Text
            //값은 IntWritable.class == Int-IntWritable
            job.setOutputValueClass(IntWritable.class);

            //맵 리듀스 실행
            boolean success = job.waitForCompletion(true);
            System.exit(success ? 0: 1);
        }

}
