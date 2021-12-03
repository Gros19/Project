#include<SoftwareSerial.h>

SoftwareSerial mySerial(2,3); 
//RX, TX 와이파이 모듈 RX와 TX를 교차시켜 연결한 후
//2번과 3번 핀을 RX, TX 통신으로 쓰겠다고 선언 

String SSID="U+Net21E4";
String PASSWORD="D08105P#66";


void httpGet(String server,String uri){

  String connect_server_cmd = "AI+CIPSTART=4,\"TCP\",\""+server+"\",80";

  mySerial.println(connect_server_cmd);

  String httpCmd = "GET "+uri;

  String cmd = "AT+CIPSEND=4,"+httpCmd.length();

  mySerial.println(cmd);

  mySerial.println(httpCmd);



}

void connectWifi(){

  String cmd = "AT+CWMODE=1";
  mySerial.println(cmd);
  cmd ="AT+CWJAP=\""+SSID+"\",\""+PASSWORD+"\"";
  mySerial.println(cmd);
  delay(1000);
  if(mySerial.find("OK")){
    Serial.println("Wifi connected");
  }else{
    Serial.println("Cannot connect to Wifi"+mySerial);
  }
}

void setup() {
  Serial.begin(9600);
  mySerial.begin(9600);
  //통신 속도 지정
  connectWifi();
  httpGet("210.192.111.122","/index.html");
}

void loop() {

  if(mySerial.available()){
    Serial.write(mySerial.read());
  }
  if(Serial.available()){
    mySerial.write(Serial.read());
  }

}
