#include <Arduino.h>
#include <Keyboard.h>

#define b1 14
#define b2 12
#define b3 10
#define b4 15
#define b5 9

void setup() {
  Serial.begin(9600);
  pinMode(b1, INPUT_PULLUP);
  pinMode(b2, INPUT_PULLUP);
  pinMode(b3, INPUT_PULLUP);
  pinMode(b4, INPUT_PULLUP);
  pinMode(b5, INPUT_PULLUP);
  Keyboard.begin();
}

void loop() {
  if (digitalRead(b1) == LOW) {
    Serial.println("B1");
    Keyboard.write('1');
    delay(150);
  } else if (digitalRead(b2) == LOW) {
    Serial.println("B2");
    Keyboard.write('2');
    delay(150);
  } else if (digitalRead(b3) == LOW) {
    Serial.println("B3");
    Keyboard.write('3');
    delay(150);
  } else if (digitalRead(b4) == LOW) {
    Serial.println("B4");
    Keyboard.write('4');
    delay(150);
  } else if (digitalRead(b5) == LOW) {
    Serial.println("B5");
    Keyboard.write(KEY_BACKSPACE);
    delay(150);
  }
}
