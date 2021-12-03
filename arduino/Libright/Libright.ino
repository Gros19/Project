#include<MsTimer2.h>
/*
 * 시간에 관한 변수
 */
int what_minute = 0;
int ONE_MINUTE = 10000;

/*
 * 수면 조건에 관한 변수
 */
int non_sleep_proof = 0;
boolean before_sleep_mode = false;

/*
 * 적외선 거리 센서에 관한 변수
 */
int rlsen = 8;
/*
 * 적외선 거리 센서의 측정값
 */
int senval = 0;

/*
 * 조건 테스트용 pin
 */
int testPin = 2;
 
/*
 * led pin에 관한 변수
 */
int ledPin[4] = {5, 6, 9, 10};

/*
 * led 밝기에 관한 변수
 */
int bright = 0;
int bright255 = 0;
int dimmer = 0;

void oneMinuteCount(){
  what_minute += 1;
  Serial.print(what_minute);
  Serial.println("분입니다.");
  /*
   * 15분이 지나면 what_minute를 0으로 초기화
   * 0 ~ 15분 반복
   */
  if(what_minute > 14*6){ 
    what_minute = 0;
    /*
     * 15분간 non_sleep_proof < 3 보다 작아 책장을 거의 넘기지 않았다명
     * before_sleep_mode로 돌입하고
     */
    if(non_sleep_proof < 5){
      before_sleep_mode = true;
      Serial.println("수면모드 돌입");
    }
    else{
      non_sleep_proof = 0;
      Serial.println("책 잘 읽는 중");
    }
  }
}
void setup() {
 Serial.begin(9500);
 //testPin ouput setting
 pinMode(testPin, OUTPUT); 

 //sensor pin input setting
 pinMode(rlsen, INPUT);
 
 //led pin output setting
 pinMode(ledPin[0], OUTPUT);
 pinMode(ledPin[1], OUTPUT);
 pinMode(ledPin[2], OUTPUT);
 pinMode(ledPin[3], OUTPUT);

 //MsTimer2 func setting
 //ONE_MINUTE * 15 time over then count func call
 MsTimer2::set(ONE_MINUTE, oneMinuteCount);

 //MsTimer2 func start
 MsTimer2::start();
}

void loop() {

  /*
   * 적외선 거리 센서의 값을 변수로 선언
   * 값을 읽어 저장
   */
  senval = digitalRead(rlsen); 
 

    /*
     * senval이 HIGH면 
     * 책을 읽고 있고, 잠을 자지 않는다고 판단
     * non_sleep_proof를 1증가 시킨다.
     */
    if(senval == HIGH){
      digitalWrite(testPin, HIGH);
      non_sleep_proof ++;
      /*
       * 만약 before_sleep_mode인 상태에서 
       * non_sleep_proof가 2보다 크다면
       * before_sleep_mode에서 탈출하고
       * 밝기를 조금 올린다.
       */
      if(non_sleep_proof > 5 and before_sleep_mode == true){
        before_sleep_mode = false;
        Serial.println("sleep모드 탈출!");
        dimmer = 0;
      }
    }
    else{
      digitalWrite(testPin, LOW);
    }

   
    

  /*
   * 밝기를 수동 조절하기 위해 rotaition senseo 사용
   * A0번 핀에서 아날로그 입력을 받고 변수 bright에 저장
   */
  bright = analogRead(0);

  /*
  입력 전압 5v일 때 0~255까지 범위를 설정하기 위해
  int bright255 = bright >> 2;
  하지만
  3.3V일 때는 0~172 범위가 축소 됌
  
  그래서
  bright >> 1 하면 0~344 이고
  */
  bright = bright >> 1;

  /* 
   * 그래서 int bright255 bright - bright * 0.3
   * 344 - (344*0.3) 하면 0 ~ 245 범위를 가짐
   */
  bright255 = bright - (bright *(0.3));

   /*
   * 만약 before_sleep_mode 모드이면
   * 밝기를  조금씩 어둡게 한다.
   */
  if(before_sleep_mode == true){
    dimmer += 25;
    
  }
  bright255 = bright255 - dimmer;
 


  
  if(bright255 > 0){
  analogWrite(ledPin[0], bright255);
  analogWrite(ledPin[1], bright255);
  analogWrite(ledPin[2], bright255);
  analogWrite(ledPin[3], bright255);  
  }
  else if (before_sleep_mode == true){
    Serial.println("수면모드이고 밝기가 0 이하니까 꺼");
    analogWrite(ledPin[0], LOW);
    analogWrite(ledPin[1], LOW);
    analogWrite(ledPin[2], LOW);
    analogWrite(ledPin[3], LOW);
    if (bright255 < -100){
      Serial.println("이제 진짜 종료");
    }
  }
  delay(500);

}
