#include<MsTimer2.h>
/*
 * 시간에 관한 변수
 */
int what_time = 0;
int minute = 4;
int TIME = 1000;


/*
 * 수면 조건에 관한 변수
 */
int non_sleep_proof = 0;
boolean before_sleep_mode = false;

/*
 * 적외선 거리 센서에 관한 변수
 */
int rlsen = 13;
/*
 * 적외선 거리 센서의 측정값
 */
int senval = 0;

/*
 * 조건 테스트용 pin
 */
int testPin = 12;

/*
 * led pin에 관한 변수
 */
int ledPinA[4] = {10, 5, 6, 9};
int ledPinB[4] = {2, 4, 7, 8};
/*
 * led 밝기에 관한 변수
 */

int bright255 = 0;
int dimmer = 1;

void LedAON();
void LedAOFF();
void LedBON();
void LedBOFF();

void oneMinuteCount(){
 ++what_time;
 /*
  * 60초가 되면
  */
 if(what_time > 59){
  /*
   * 시간을 0초로 초기화
   */
  oneFiveMinuteProc();
  what_time = 0;

   /*
   * 만약 before_sleep_mode 모드이면
   * 밝기를 조금씩 어둡게 한다.
   */
  if(before_sleep_mode == true){
    dimmer += 34;
    bright255 -= dimmer;
  }else{
    bright255 = analogRead(0)>>2;
  }
  Serial.println(bright255);
 }
}

void oneFiveMinuteProc(){
  ++minute;
  /*
   * 5분이되면
   */
  if(minute > 4){
    /*
     * 0분으로 초기화
     */
    minute = 0;
    /*
     * 5분동안 책을 안 읽었으면
     * 슬립모드로 전환
     */
    if(non_sleep_proof < 2){
      before_sleep_mode = true;
    }else{
      before_sleep_mode =false;
      non_sleep_proof = 0;
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
 pinMode(ledPinA[0], OUTPUT);
 pinMode(ledPinA[1], OUTPUT);
 pinMode(ledPinA[2], OUTPUT);
 pinMode(ledPinA[3], OUTPUT);
 pinMode(ledPinB[0], OUTPUT);
 pinMode(ledPinB[1], OUTPUT);
 pinMode(ledPinB[2], OUTPUT);
 pinMode(ledPinB[3], OUTPUT);

 //MsTimer2 func setting
 //TIME * 15 time over then count func call
 MsTimer2::set(TIME, oneMinuteCount);

 //MsTimer2 func start
 MsTimer2::start();
 
 bright255 = analogRead(0)>>2;
 Serial.println(bright255);
 Serial.println("strart!");
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
      delay(3000);
      /*
       * 만약 before_sleep_mode인 상태에서
       * non_sleep_proof가 2보다 크다면
       * before_sleep_mode에서 탈출하고
       * 밝기를 조금 올린다.
       */
      if(before_sleep_mode == true and non_sleep_proof > 2){
        before_sleep_mode = false;
        Serial.println("sleep모드 탈출!");
        non_sleep_proof = 0;
        bright255 = analogRead(0)>>2;
        dimmer = 0;
        LedBON();
      }
    }
    else{
      digitalWrite(testPin, LOW);
    }


  if(before_sleep_mode != true){
   
    if(20 > bright255){
      bright255 = 0;
      LedBOFF();
    }else if(135 > bright255){
      LedBOFF();
    }else if(134 < bright255 && bright255 < 171){
      LedBHalfON();
    }else if(bright255 > 170){
       LedBON();
    }
    LedAON();
   
  }else if (bright255 >= 0){
    if(20 > bright255){
      bright255 = 0;
      LedBOFF();
    }else if(135 > bright255){
      LedBOFF();
    }else if(134 < bright255 && bright255 < 171){
      LedBHalfON();
    }else if(bright255 > 170){
       LedBON();
    }
    LedAON();
    
  }else{
    Serial.print("sleep");
    before_sleep_mode = false;
    LedAOFF();
    LedBOFF();
    dimmer = 0;
    delay(8*360000);
    /*
     *   8시간 후 다시 켜기
     */
  }
  delay(500);
}


void LedAON(){
    analogWrite(ledPinA[0], bright255);
    analogWrite(ledPinA[1], bright255);
    analogWrite(ledPinA[2], bright255);
    analogWrite(ledPinA[3], bright255);
}

void LedAOFF(){
    analogWrite(ledPinA[0], LOW);
    analogWrite(ledPinA[1], LOW);
    analogWrite(ledPinA[2], LOW);
    analogWrite(ledPinA[3], LOW);
}

void LedBON(){
    digitalWrite(ledPinB[0], HIGH);
    digitalWrite(ledPinB[1], HIGH);
    digitalWrite(ledPinB[2], HIGH);
    digitalWrite(ledPinB[3], HIGH);
}
void LedBHalfON(){
    digitalWrite(ledPinB[0], HIGH);
    digitalWrite(ledPinB[1], LOW);
    digitalWrite(ledPinB[2], HIGH);
    digitalWrite(ledPinB[3], LOW);
}
void LedBOFF(){
    digitalWrite(ledPinB[0], LOW);
    digitalWrite(ledPinB[1], LOW);
    digitalWrite(ledPinB[2], LOW);
    digitalWrite(ledPinB[3], LOW);
}
