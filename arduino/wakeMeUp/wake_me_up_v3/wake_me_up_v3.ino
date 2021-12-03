    /*
     * v.3 스누즈 기능을 제거야였고
     * 코드를 간단하게 다듬었다.
     */
    
    #include<Servo.h>
    Servo servo;
    /*서보 모터는 9번핀 사용*/
    int servoPin = 9;
    
    /*사운드 센서는 A0핀 사용*/
    int SoundSensor = A0; 

    /*소리 감지 횟수*/
    int count = 0;
    
    
    void setup() {           
      /*시리얼 통신 속도 설정*/    
      Serial.begin(9600); 
      /*A0에서 입력을 받겠다*/
      pinMode(SoundSensor,INPUT);
      /*9번핀을 서보모터에 사용*/
      servo.attach(servoPin);
      servo_OFF();
    }

    void loop() {
      /*아날로그 센서값을 읽어들여 level에 저장*/     
      int level = analogRead(SoundSensor);
      Serial.println(level);
      
      delay(50);
      /*소리 값이 8보다 크면 count+1 */
      if (level > 8){
        count++;
        Serial.print("#######: ");
        Serial.println(count);
      }
      /*count가 10 초과면 현재 알람이 울리고 있다고 가정, 모터 작동해서 방에 불을 킴*/
      if (count > 10){
        Serial.println("");
        Serial.print("Light On");
        Serial.println("");
        /*서보모터 작동*/
        servo_do();
        /*카운트는 다시 0으로 초기화*/
        count = 0;
       
      }
    }



    void servo_do()
    {
        servo_ON();
        delay(300);
        servo_OFF();
        delay(300);
    }

    void servo_ON()
    {
      servo.attach(servoPin);
      servo.write(78);
      delay(500);
      servo.detach();
    }

    void servo_OFF()
    {
      servo.attach(servoPin);
      servo.write(2);
      delay(500);
      servo.detach();
    }

  
