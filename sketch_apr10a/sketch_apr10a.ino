
#include <Servo.h>

Servo sv;  //Servo motor's object

int sensor = 0;

void setup()
{
  pinMode(A0, INPUT); //Gas analog input
  pinMode(12,OUTPUT); //Setting up esp8266
  sv.attach(9);       // Servo motor initialization
  Serial.begin(9600);  // Activate the serial monitor
  digitalWrite(12,LOW); //to set esp8266 =back to orignal when hit reset
   sv.write(0);         //to set servo = back to orignal when hit reset
}


void loop()
{

  sensor = analogRead(A0);  //storing sensor value in to variable
  Serial.println(sensor);    //printing on the console
  if(sensor>250){

    sv.write(180);        //rotating the motor
    
    digitalWrite(12,HIGH); //start the esp for syncing with firebase

  }
  delay(10); // Delay a little bit to improve simulation performance
}
