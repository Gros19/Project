#include<MsTimer2.h>
/*
 * 시간에 관한 변수
 */
int what_time = 0;
int minute = 0;
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
    dimmer += 17;
  }else{
    bright255 = analogRead(0)>>2;
  }
 }
}

void oneFiveMinuteProc(){
  ++minute;
  /*
   * 15분이되면
   */
  if(minute > 14){
    /*
     * 0분으로 초기화
     */
    minute = 0;
    /*
     * 15분동안 책을 안 읽었으면
     * 슬립모드로 전환
     */
    if(non_sleep_proof < 5){
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
 Serial.println("strart!");
 bright255 = 255;
 LedBON();
 LedAON();

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
      delay(1000);
      /*
       * 만약 before_sleep_mode인 상태에서
       * non_sleep_proof가 2보다 크다면
       * before_sleep_mode에서 탈출하고
       * 밝기를 조금 올린다.
       */
      if(before_sleep_mode == true and non_sleep_proof > 5){
        before_sleep_mode = false;
        Serial.println("sleep모드 탈출!");
        non_sleep_proof = 0;
        dimmer = 0;
        LedBON();
      }
    }
    else{
      digitalWrite(testPin, LOW);
    }



    if(bright255 < 11){
      bright255 = 0;
    }



  if(before_sleep_mode != true){
//    Serial.println("슬립모드가 아니다");
    LedAON();
    LedBON();
  }
  else if (bright255 > 0){
    LedAON();
    LedBOFF();
    Serial.print(bright255);
    Serial.print(" - ");
    Serial.print(dimmer);
    Serial.print(" = ");
    bright255 = bright255 - dimmer;
    Serial.print(bright255);
    Serial.println("슬립모드이고 밝기가 양수");

  }else{
    Serial.println("슬립모드이고 밝기가 0이하");
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
void LedBOFF(){
    digitalWrite(ledPinB[0], LOW);
    digitalWrite(ledPinB[1], LOW);
    digitalWrite(ledPinB[2], LOW);
    digitalWrite(ledPinB[3], LOW);
}


