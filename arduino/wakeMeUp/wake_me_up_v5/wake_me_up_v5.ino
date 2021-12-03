
    /*
     *  v.4
     *  MsTimer2 라이브러리를 활용하여 
     *  오작동을 방지하는 reset기능을 추가했다.
     *  v.5
     *  fnd를 추가했기 때문에 서보모터 디지털 핀을 D0 RX핀으로 수정 
     */
     
    #include<Servo.h>
    #include<MsTimer2.h>

    
    Servo servo;
    /*서보 모터는 0번핀 사용*/
    int servoPin = 0;
    /*서보모터 시간제어*/
    unsigned long time_previous, time_current , tp ,tc;
    /*서보모터 스위치*/
    int servoSW = 0; // 0은 열린 상태, 1은 닫힌 상태
    /*사운드 센서는 A0핀 사용*/
    int SoundSensor = A0; 

    /*입력받은 소리 level과 비교하기 위한 limit 변수*/
    int limit = 8;
    
    /*소리 감지 횟수*/
    int count = 0;
    /*reset을 위한 비교변수*/
    int Rcount = 555;
    

    
    /*이하 FND 관련 선언*/
    //7세그먼트 선택 핀
    int digit_select_pin[] = {10,11,12,13};
    //7세그먼트 제어 핀
    int segment_pin[] = {2,3,4,5,6,7,8,9};
    //자릿수 표현 사이의 지연 시간, ms 단위
    int time_delay = 1;
    //0~9까지의 숫자 표시를 위한 세그먼트 a~dp의 점멸 패턴
    //켜지는 부분이 1로 표현됨
    byte digits_data[10]={0xFC, 0x60, 0xDA, 0xF2,  0x66, 
                          0xB6, 0xBE, 0xE4, 0xFE, 0xE6};
    int f_level_1000, f_level_100, f_limit_10, f_limit_1;
   
    /*이상 FND 관련 선언*/
    
    void reset_Count(){
      if(count == Rcount){
        count = 0;
        }
      
      Rcount = count;
    }

    
    void setup() {           
      /*시리얼 통신 속도 설정*/    
      Serial.begin(9600); 
      /*A0에서 입력을 받겠다*/
      pinMode(SoundSensor,INPUT);
      /*9번핀을 서보모터에 사용*/
      servo.attach(servoPin);
      servo.write(2);
      delay(500);
      servo.detach();
      
      MsTimer2::set(15000, reset_Count);  //10s period
      MsTimer2::start();

      //자릿수 선택 핀을 출력으로 설정
      for(int i = 0; i < 4; i++)
      {
        pinMode(digit_select_pin[i], OUTPUT);
      }
      // 세그먼트 제어 핀을 풀력으로 설정
      for(int i = 0; i < 8; i++)
      {
        pinMode(segment_pin[i], OUTPUT);
      }
      tp = millis();
    }
    
    void loop() {
      /*아날로그 센서값을 읽어들여 level에 저장*/     
      int level = analogRead(SoundSensor);
        tc = millis();
        if(tc - tp > 100){
           f_level_1000 = level /1000;
           f_level_100 = level % 1000/100;
           f_limit_10 = limit % 100/10;
           f_limit_1 = limit % 10;
           tp = millis();
           Serial.print(f_level_1000);
           Serial.print(f_level_100);
           Serial.print(f_limit_10);
           Serial.println(f_limit_1);
        }
       
       
     
        show_digit(1, f_level_1000);
        delay(time_delay);
        show_digit(2, f_level_100);
        delay(time_delay);
        show_digit(3, f_limit_10);
        delay(time_delay);
        show_digit(4, f_limit_1);
        delay(time_delay);
      
      /*소리 값이 limit값(디폴트는 8)보다 크면 count+1 */
      if (level > limit){
        count++;
      }
      /*count가 10 초과면 현재 알람이 울리고 있다고 가정, 모터 작동해서 방에 불을 킴*/
      if (count > 10){
        Serial.println("");
        Serial.print("Light On");
        Serial.println("");
        /*서보모터 작동*/
        if(servoSW == 0){
          time_previous = millis(); // 시작시간
          servoSW = 1;  //닫아라
        }
        
        servo_do();
      }
    }



    void servo_do()
    {
        Serial.println("servo do");
        Serial.println(servoSW);
        if(servoSW == 1)
        {
            servo.attach(servoPin);
            servo.write(78);
            time_current = millis();
            if (time_current - time_previous> 500){
              Serial.println("servo 1");
              Serial.println(time_current - time_previous);
              
              servoSW = 11;
              servo.detach();
            }
        }
        else if(servoSW == 11){
          time_current = millis();
          Serial.println("servo 11");
          Serial.println(time_current - time_previous);
          if (time_current - time_previous > 800){
            servoSW = 2;
            Serial.println("servo 2");
          }
        }

        else if(servoSW == 2){
          time_current = millis();
          Serial.println("servo off");
              servo.attach(servoPin);
              servo.write(2);
              if (time_current - time_previous > 1300){
              servoSW = 22;
              servo.detach();
              }
        }
         else if(servoSW == 22){
          time_current = millis();
          if (time_current - time_previous > 1600){
          /*카운트는 다시 0으로 초기화*/
          count = -5;
          /*survoSW를 다시 열어놓고 함수 종료*/
          servoSW = 0;
          
          
          }
        }
        Serial.println("");
          Serial.print("servo End");
        
    }

  
    //해당 자릿수에 숫자 하나를 표시하는 함수
    //(위치, 출력할 숫자)
    void show_digit(int pos, int number)
    {
      for(int i = 0; i < 4; i++)
      {
        if(i+1 ==pos)
          //해당 자릿수의 선택 핀만 LOW로 설정
          digitalWrite(digit_select_pin[i], LOW);
        else
          //나머지 자리는 HIGH로 설정
          digitalWrite(digit_select_pin[i], HIGH);
      }
      // 8개 세그먼트 제어
      for(int i = 0; i < 8; i++)
      {
        byte segment_data = (digits_data[number] 
             & (0x01 << i)) >> i;
        if(segment_data == 1)
          digitalWrite(segment_pin[7-i], HIGH);
        else
          digitalWrite(segment_pin[7-i], LOW);
      }
    }
      
