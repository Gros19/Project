package mongo;

import lombok.extern.log4j.Log4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

@Log4j
public class AccessLog extends Configuration implements Tool {
    public static void main(String[] args) throws Exception {
        /*분석할 파일(폴더)과 분석 결과가 저장될 파일(폴더)로 2개를 받음*/
        if(args.length != 2){
            log.info("분석할 폴더(파일) 및 분석결과가 저장될 폴더를 입력해야 합니다.");
            System.exit(-1);
        }
        int exitCode = ToolRunner.run(new AccessLog(), args);

        System.exit(exitCode);
    }
    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = this.getConf();
        String appName = conf.get("AppName");

        log.info("appName :" + appName);

        /*메모리에 올린 하둡분산파일 시스템에 저장된 파일명*/
        String cacheFile = "/access_log";

        /*맵리듀스 실행을 위한 잡 객체를 가져오기*/
        Job job = Job.getInstance(conf);

        /*호출이 발생하면, 메모리에 저장하여 캐시 처리 수행*/
        job.addCacheFile(new Path(cacheFile).toUri());

        /*맵리듀스 잡이 시작되는 main함수가 존재하는 파일설정*/
        job.setJarByClass(AccessLog.class);

        /*맵리듀스 잡 이름 설정, 맵리듀스 실행 결과 및 로그 확인할 때 편리*/
        job.setJobName(appName);

        /*분석할 폴더(파일) --첫번째 파라미터*/
        FileInputFormat.setInputPaths(job, new Path(strings[0]));

        /*분석할 결과가 저장되는 폴더(파일) -- 두번쨰 파라미터*/
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));

        /*맵리듀스 맵 역할을 수행하는 Mapper 자바 파일 설정*/
        job.setMapperClass(AccessLogMapper.class);

        /*모든 로그 내용을 MongoDB로 저장하는 작업은 Shuffle and Sort가 필요없음*/
        job.setNumReduceTasks(0);

        /*결과 파일을 생성하지 않기 때문에
        * job.setOutputValueClass()
        * job.setOutputKetClass() 가 필요 없음*/

        /*맵리듀스 실행*/
        boolean success = job.waitForCompletion(true);
        return (success ? 0 : 1);
    }

    @Override
    public void setConf(Configuration configuration) {
        /*앱이름 정의*/
        configuration.set("AppName", "AccessLog MongoDB Test");

    }

    @Override
    public Configuration getConf() {

        /*맵리듀스 전체에 적용될 변수를 정의할 때 사용*/
        Configuration conf = new Configuration();

        /*변수 정의*/
        this.setConf(conf);

        return conf;
    }
}
