#define trigPin 13
#define echoPin 12
int pushButton = 7;
int light=0;
boolean start = false;
boolean buttonState = 0;

void setup() {
  Serial.begin(9600);
  pinMode(trigPin, OUTPUT); //trigPin을 출력 핀으로 설정
  pinMode(echoPin, INPUT);  //echoPin을 입력핀으로 설정하빈다.
  pinMode(pushButton, INPUT);
}

void downLight(){
        
    long duration, distance;
    for(; light >= 0; light--){
           buttonState = digitalRead(pushButton);
           delay(100); 
          if(buttonState==1){
             analogWrite(3, 0);
             analogWrite(5, 0);
             analogWrite(6, 0);
             analogWrite(9, 0);
             analogWrite(11, 0);
            break;
            
          }
          digitalWrite(trigPin, LOW);
          delayMicroseconds(2);
          digitalWrite(trigPin, HIGH);
          delayMicroseconds(10);
          digitalWrite(trigPin, LOW);
          duration = pulseIn(echoPin, HIGH);
          distance = duration * 17 / 1000; //duration을 연산하여 센싱한 거리값을 distance에 저장
          if (distance >= 200 || distance <= 0) // 거리가 200cm이 넘거나 0보다 작으면
          {
            Serial.println("거리를 측정할 수 없음"); // 에러를 출력합니다.
          }
          else if(distance < 5 && start == true){
            light = 255;
          }
      
    analogWrite(3, light);
    analogWrite(5, light);
    analogWrite(6, light);
    analogWrite(9, light);
    analogWrite(11, light);
    delay(20);
    }
    start = false;
}

void loop() {
  
  buttonState = digitalRead(pushButton);
  delay(1000);
  Serial.println(buttonState);
  if (buttonState == 1){
      light = 255;
      downLight();
    }
    buttonState = 0;
}
