    /*
     *  v.4
     *  MsTimer2 라이브러리를 활용하여 
     *  오작동을 방지하는 reset기능을 추가했다.
     */
     
    #include<Servo.h>
    #include<MsTimer2.h>

    
    Servo servo;
    /*서보 모터는 9번핀 사용*/
    int servoPin = 9;
    
    /*사운드 센서는 A0핀 사용*/
    int SoundSensor = A0; 

    /*소리 감지 횟수*/
    int count = 0;
    /*reset을 위한 비교변수*/
    int Rcount = 555;

    

    
    void reset_Count(){
      if(count == Rcount){
        Serial.println("##TimeOutAndReset##");
        Serial.print("count: ");
        Serial.print(count);
        Serial.print("\tRcount: ");
        Serial.println(Rcount);
        Serial.println("#######");
        count = 0;
      }
        Serial.print("count: ");
        Serial.print(count);
        Serial.print("\tRcount: ");
        Serial.println(Rcount);
      Rcount = count;
    }

    
    void setup() {           
      /*시리얼 통신 속도 설정*/    
      Serial.begin(9600); 
      /*A0에서 입력을 받겠다*/
      pinMode(SoundSensor,INPUT);
      /*9번핀을 서보모터에 사용*/
      servo.attach(servoPin);
      servo_OFF();
      
      MsTimer2::set(20000, reset_Count);  //20s period
      MsTimer2::start();
    }
    
    void loop() {
      /*아날로그 센서값을 읽어들여 level에 저장*/     
      int level = analogRead(SoundSensor);
      Serial.println(level);
      
      delay(50);
      /*소리 값이 8보다 크면 count+1 */
      if (level > 8){
        count++;
        Serial.println("#######");
        Serial.print("count: ");
        Serial.print(count);
        Serial.print("\tRcount: ");
        Serial.println(Rcount);
        Serial.println("#######");
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

  
